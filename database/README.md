# Database

## Table of Contents

- [Overview](#overview)
- [Prerequisites](#prerequisites)
- [Quickstart](#quickstart)
- [CI/CD Pipeline](#cicd-pipeline)
- [Contributing](#contributing)
- [License](#license)

## Overview

...

## Prerequisites

- **Java 21**
- **Maven 3.9+**
- **Docker** (for local deployment)
- **PostgreSQL** (or use Docker)
- A reachable PostgreSQL (see ../infrastructure/README.md for the GCP Cloud SQL set-up)

## Quickstart

First, start the `LOCAL_DEV` database with

```bash
docker compose up -d
```

inside the directory `database/docker`. Afterward, run the following command to apply the migrations from the root
repository:

```bash
# from repository root
mvn clean compile \
-Dpostgresdb.url=jdbc:postgresql://localhost:5432/ppmp \
-Dpostgresdb.user=<username> \
-Dpostgresdb.password=<password> \
-f pom.xml
```

What this does:

- The liquibase-maven-plugin is bound to the Maven compile phase by the pom.xml. When Maven runs compile, the plugin
  runs
  update against the configured database URL and credentials, applying changelogs to the target database.
- Ensure the PostgreSQL instance is running at the provided JDBC URL and the user exists with the given password and
  privileges.
- For local testing you can use the mentioned local docker run postgres instance or any dev database.


## CI/CD Pipeline

### Pipeline Overview

...

### CI/CD Variables

...

# Contributing

...

# License

Proprietary - All rights reserved
