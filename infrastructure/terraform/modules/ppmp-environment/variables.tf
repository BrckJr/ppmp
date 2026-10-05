variable "project_id" {
  description = "GCP project of this environment (one dedicated project per environment)."
  type        = string
}

variable "region" {
  description = "GCP region for all resources."
  type        = string
  default     = "europe-west3"
}

variable "environment" {
  description = "Environment name, e.g. dev or prod. Must match the GitHub environment of the same name."
  type        = string
}

variable "github_repository" {
  description = "GitHub repository (owner/name) that is allowed to deploy into this project."
  type        = string
}

variable "backend_min_instances" {
  description = "Minimum Cloud Run instances of the backend. 0 = scale to zero (cheapest, cold starts)."
  type        = number
  default     = 0
}

variable "backend_max_instances" {
  type    = number
  default = 3
}

variable "backend_memory" {
  type    = string
  default = "1Gi"
}

variable "db_tier" {
  description = "Cloud SQL machine tier."
  type        = string
  default     = "db-f1-micro"
}

variable "db_availability_type" {
  description = "ZONAL or REGIONAL (high availability)."
  type        = string
  default     = "ZONAL"
}

variable "db_deletion_protection" {
  type    = bool
  default = true
}

variable "image_source_project" {
  description = "Project whose Artifact Registry this environment may read images from (promotion DEV -> PROD). Null for DEV."
  type        = string
  default     = null
}
