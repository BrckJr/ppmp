terraform {
  required_version = ">= 1.5"

  # Bucket is configured via backend.hcl (terraform init -backend-config=backend.hcl)
  backend "gcs" {}

  required_providers {
    google = {
      source  = "hashicorp/google"
      version = "~> 6.0"
    }
    random = {
      source  = "hashicorp/random"
      version = "~> 3.6"
    }
  }
}

provider "google" {
  project = var.project_id
  region  = var.region
}

module "ppmp" {
  source = "../../modules/ppmp-environment"

  project_id        = var.project_id
  region            = var.region
  environment       = "prod"
  github_repository = var.github_repository

  backend_min_instances  = 1
  db_tier                = "db-g1-small"
  db_availability_type   = "ZONAL" # switch to REGIONAL for high availability (doubles DB cost)
  db_deletion_protection = true

  # PROD only ever receives images that were built and tested on DEV
  image_source_project = var.dev_project_id
}

variable "dev_project_id" {
  description = "GCP project id of the DEV environment."
  type        = string
}

variable "project_id" {
  type = string
}

variable "region" {
  type    = string
  default = "europe-west3"
}

variable "github_repository" {
  type    = string
  default = "BrckJr/ppm-platform"
}

output "frontend_url" {
  value = module.ppmp.frontend_url
}

output "github_environment_variables" {
  value = module.ppmp.github_environment_variables
}
