locals {
  name            = "ppmp-${var.environment}"
  registry_host   = "${var.region}-docker.pkg.dev"
  repository_id   = "ppmp"
  db_name         = "ppmp"
  db_user         = "ppmp-bot"
  placeholder_img = "us-docker.pkg.dev/cloudrun/container/hello"

  services = [
    "artifactregistry.googleapis.com",
    "iam.googleapis.com",
    "iamcredentials.googleapis.com",
    "run.googleapis.com",
    "secretmanager.googleapis.com",
    "sqladmin.googleapis.com",
    "sts.googleapis.com",
  ]
}

resource "google_project_service" "enabled" {
  for_each           = toset(local.services)
  service            = each.value
  disable_on_destroy = false
}

# ---------------------------------------------------------------------------
# Container registry
# ---------------------------------------------------------------------------
resource "google_artifact_registry_repository" "images" {
  repository_id = local.repository_id
  location      = var.region
  format        = "DOCKER"
  description   = "PPMP backend and frontend images (${var.environment})"

  # Keep the registry small: images are immutable, tagged by commit SHA
  cleanup_policy_dry_run = false
  cleanup_policies {
    id     = "keep-last-20"
    action = "KEEP"
    most_recent_versions {
      keep_count = 20
    }
  }
  cleanup_policies {
    id     = "delete-old"
    action = "DELETE"
    condition {
      older_than = "2592000s" # 30 days
    }
  }

  depends_on = [google_project_service.enabled]
}

# ---------------------------------------------------------------------------
# Database: Cloud SQL for PostgreSQL
# Reachable only through the Cloud SQL connector (IAM + TLS), no authorized networks.
# ---------------------------------------------------------------------------
resource "google_sql_database_instance" "db" {
  name                = "${local.name}-db"
  region              = var.region
  database_version    = "POSTGRES_17"
  deletion_protection = var.db_deletion_protection

  settings {
    edition           = "ENTERPRISE"
    tier              = var.db_tier
    availability_type = var.db_availability_type
    disk_type         = "PD_SSD"
    disk_size         = 10
    disk_autoresize   = true

    deletion_protection_enabled = var.db_deletion_protection

    ip_configuration {
      ipv4_enabled = true
      ssl_mode     = "ENCRYPTED_ONLY"
    }

    backup_configuration {
      enabled                        = true
      start_time                     = "02:00"
      point_in_time_recovery_enabled = var.environment == "prod"
    }

    maintenance_window {
      day  = 7
      hour = 3
    }
  }

  depends_on = [google_project_service.enabled]
}

resource "google_sql_database" "app" {
  name     = local.db_name
  instance = google_sql_database_instance.db.name
}

resource "random_password" "db" {
  length  = 32
  special = false
}

resource "google_sql_user" "app" {
  name     = local.db_user
  instance = google_sql_database_instance.db.name
  password = random_password.db.result
}

# ---------------------------------------------------------------------------
# Secrets
# ---------------------------------------------------------------------------
resource "random_id" "jwt" {
  byte_length = 48 # >= 32 bytes required, b64_url is the format the backend expects
}

resource "google_secret_manager_secret" "db_password" {
  secret_id = "${local.name}-db-password"
  replication {
    auto {}
  }
  depends_on = [google_project_service.enabled]
}

resource "google_secret_manager_secret_version" "db_password" {
  secret      = google_secret_manager_secret.db_password.id
  secret_data = random_password.db.result
}

resource "google_secret_manager_secret" "jwt" {
  secret_id = "${local.name}-jwt-secret"
  replication {
    auto {}
  }
  depends_on = [google_project_service.enabled]
}

resource "google_secret_manager_secret_version" "jwt" {
  secret      = google_secret_manager_secret.jwt.id
  secret_data = random_id.jwt.b64_url
}

# ---------------------------------------------------------------------------
# Service accounts (least privilege)
# ---------------------------------------------------------------------------
resource "google_service_account" "backend" {
  account_id   = "${local.name}-backend"
  display_name = "PPMP backend runtime (${var.environment})"
  depends_on   = [google_project_service.enabled]
}

resource "google_project_iam_member" "backend_sql" {
  project = var.project_id
  role    = "roles/cloudsql.client"
  member  = "serviceAccount:${google_service_account.backend.email}"
}

resource "google_secret_manager_secret_iam_member" "backend_secrets" {
  for_each  = { db = google_secret_manager_secret.db_password.id, jwt = google_secret_manager_secret.jwt.id }
  secret_id = each.value
  role      = "roles/secretmanager.secretAccessor"
  member    = "serviceAccount:${google_service_account.backend.email}"
}

resource "google_service_account" "frontend" {
  account_id   = "${local.name}-frontend"
  display_name = "PPMP frontend runtime (${var.environment})"
  depends_on   = [google_project_service.enabled]
}

# ---------------------------------------------------------------------------
# Cloud Run
# The pipeline owns the container image; Terraform owns everything else.
# ---------------------------------------------------------------------------
resource "google_cloud_run_v2_service" "backend" {
  name                = "${local.name}-backend"
  location            = var.region
  ingress             = "INGRESS_TRAFFIC_ALL"
  deletion_protection = var.environment == "prod"

  template {
    service_account = google_service_account.backend.email

    scaling {
      min_instance_count = var.backend_min_instances
      max_instance_count = var.backend_max_instances
    }

    containers {
      image = local.placeholder_img

      resources {
        limits = {
          cpu    = "1"
          memory = var.backend_memory
        }
        cpu_idle          = true
        startup_cpu_boost = true
      }

      env {
        name  = "QUARKUS_DATASOURCE_JDBC_URL"
        value = "jdbc:postgresql:///${local.db_name}?cloudSqlInstance=${google_sql_database_instance.db.connection_name}&socketFactory=com.google.cloud.sql.postgres.SocketFactory"
      }
      env {
        name  = "QUARKUS_DATASOURCE_USERNAME"
        value = local.db_user
      }
      env {
        name  = "QUARKUS_HIBERNATE_ORM_LOG_SQL"
        value = "false"
      }
      env {
        name = "QUARKUS_DATASOURCE_PASSWORD"
        value_source {
          secret_key_ref {
            secret  = google_secret_manager_secret.db_password.secret_id
            version = "latest"
          }
        }
      }
      env {
        name = "PPMP_JWT_SECRET"
        value_source {
          secret_key_ref {
            secret  = google_secret_manager_secret.jwt.secret_id
            version = "latest"
          }
        }
      }
    }
  }

  lifecycle {
    ignore_changes = [template[0].containers[0].image, client, client_version]
  }

  depends_on = [
    google_secret_manager_secret_version.db_password,
    google_secret_manager_secret_version.jwt,
    google_secret_manager_secret_iam_member.backend_secrets,
    google_project_iam_member.backend_sql,
  ]
}

# The API is public at network level; every endpoint except login/register/health is protected by the
# application's own authentication (deny by default).
resource "google_cloud_run_v2_service_iam_member" "backend_public" {
  name     = google_cloud_run_v2_service.backend.name
  location = var.region
  role     = "roles/run.invoker"
  member   = "allUsers"
}

# The frontend container serves the SPA and reverse-proxies /api to the backend, which keeps browser
# and API on ONE origin (required for the SameSite=Strict session cookie, no CORS needed).
resource "google_cloud_run_v2_service" "frontend" {
  name                = "${local.name}-frontend"
  location            = var.region
  ingress             = "INGRESS_TRAFFIC_ALL"
  deletion_protection = var.environment == "prod"

  template {
    service_account = google_service_account.frontend.email

    scaling {
      min_instance_count = 0
      max_instance_count = 3
    }

    containers {
      image = local.placeholder_img

      resources {
        limits = {
          cpu    = "1"
          memory = "256Mi"
        }
        cpu_idle = true
      }

      env {
        name  = "BACKEND_URL"
        value = google_cloud_run_v2_service.backend.uri
      }
      env {
        name  = "BACKEND_HOST"
        value = trimprefix(google_cloud_run_v2_service.backend.uri, "https://")
      }
    }
  }

  lifecycle {
    ignore_changes = [template[0].containers[0].image, client, client_version]
  }
}

resource "google_cloud_run_v2_service_iam_member" "frontend_public" {
  name     = google_cloud_run_v2_service.frontend.name
  location = var.region
  role     = "roles/run.invoker"
  member   = "allUsers"
}

# ---------------------------------------------------------------------------
# GitHub Actions deployer: keyless auth via Workload Identity Federation
# ---------------------------------------------------------------------------
resource "google_iam_workload_identity_pool" "github" {
  workload_identity_pool_id = "github"
  display_name              = "GitHub Actions"
  depends_on                = [google_project_service.enabled]
}

resource "google_iam_workload_identity_pool_provider" "github" {
  workload_identity_pool_id          = google_iam_workload_identity_pool.github.workload_identity_pool_id
  workload_identity_pool_provider_id = "github"
  display_name                       = "GitHub OIDC"

  attribute_mapping = {
    "google.subject"        = "assertion.sub"
    "attribute.repository"  = "assertion.repository"
    "attribute.environment" = "assertion.environment"
  }
  # Only tokens issued for this repository are accepted at all
  attribute_condition = "assertion.repository == \"${var.github_repository}\""

  oidc {
    issuer_uri = "https://token.actions.githubusercontent.com"
  }
}

resource "google_service_account" "deployer" {
  account_id   = "${local.name}-deployer"
  display_name = "GitHub Actions deployer (${var.environment})"
  depends_on   = [google_project_service.enabled]
}

# Only jobs running in the GitHub environment with this name can impersonate the deployer
resource "google_service_account_iam_member" "deployer_wif" {
  service_account_id = google_service_account.deployer.name
  role               = "roles/iam.workloadIdentityUser"
  member             = "principalSet://iam.googleapis.com/${google_iam_workload_identity_pool.github.name}/attribute.environment/${var.environment}"
}

resource "google_artifact_registry_repository_iam_member" "deployer_push" {
  repository = google_artifact_registry_repository.images.name
  location   = var.region
  role       = "roles/artifactregistry.writer"
  member     = "serviceAccount:${google_service_account.deployer.email}"
}

resource "google_cloud_run_v2_service_iam_member" "deployer_run" {
  for_each = {
    backend  = google_cloud_run_v2_service.backend.name
    frontend = google_cloud_run_v2_service.frontend.name
  }
  name     = each.value
  location = var.region
  role     = "roles/run.developer"
  member   = "serviceAccount:${google_service_account.deployer.email}"
}

resource "google_service_account_iam_member" "deployer_act_as" {
  for_each = {
    backend  = google_service_account.backend.name
    frontend = google_service_account.frontend.name
  }
  service_account_id = each.value
  role               = "roles/iam.serviceAccountUser"
  member             = "serviceAccount:${google_service_account.deployer.email}"
}

# Database migrations run from the pipeline through the Cloud SQL Auth Proxy
resource "google_project_iam_member" "deployer_sql" {
  project = var.project_id
  role    = "roles/cloudsql.client"
  member  = "serviceAccount:${google_service_account.deployer.email}"
}

resource "google_secret_manager_secret_iam_member" "deployer_db_password" {
  secret_id = google_secret_manager_secret.db_password.id
  role      = "roles/secretmanager.secretAccessor"
  member    = "serviceAccount:${google_service_account.deployer.email}"
}

# PROD promotes the exact images that ran on DEV: allow reading DEV's registry
resource "google_artifact_registry_repository_iam_member" "deployer_promote" {
  count      = var.image_source_project == null ? 0 : 1
  project    = var.image_source_project
  location   = var.region
  repository = local.repository_id
  role       = "roles/artifactregistry.reader"
  member     = "serviceAccount:${google_service_account.deployer.email}"
}
