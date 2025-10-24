# Ecommerce Java Backend

Backend developed in **Java 17** using **Spring Boot** and **Spring Data JPA**, with **PostgreSQL** as the database. This project handles the core logic for an ecommerce system, including user, role, and product management.

## Technologies

- Java 17
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Docker & Docker Compose
- Maven

## Features

- User and role management
- Product management
- Data persistence with JPA/Hibernate
- Database configuration via environment variables
- Dockerized application and database
- Auto-creation of tables and initial data in PostgreSQL

## Setup

1. Clone the repository:

```bash
git clone https://github.com/egonzalias/ecommerce-java-backend.git
cd ecommerce-java-backend
```

2. Create a `.env` file in the root with the configuration variables:

```env
POSTGRES_DB=ecommerce
POSTGRES_USER=postgres
POSTGRES_PASSWORD=admin
SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/${POSTGRES_DB}
SPRING_DATASOURCE_USERNAME=${POSTGRES_USER}
SPRING_DATASOURCE_PASSWORD=${POSTGRES_PASSWORD}
SPRING_DATASOURCE_DRIVER_CLASS_NAME=org.postgresql.Driver

SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_JPA_DATABASE_PLATFORM=org.hibernate.dialect.PostgreSQLDialect
SPRING_JPA_SHOW_SQL=true

SERVER_PORT=8080
```

3. Start the application with Docker Compose:

```bash
docker-compose up --build
```

- Database will be available at `localhost:5432`
- Backend API will be available at `http://localhost:8080`

## Project Structure

```
src/
└─ main/
   ├─ java/
   │  └─ co/com/egonzalias/
   │     └─ ... (controllers, services, repositories, models)
   └─ resources/
      └─ application.properties / application.yml
target/
.env
Dockerfile
docker-compose.yml
```

## Notes

- Tables and initial data are automatically created when the PostgreSQL container starts using the `db/init.sql` script.
- Adjust the variables in `.env` according to your local environment.

## API Endpoints (Insomnia Collection)

We include an Insomnia workspace JSON file with all the API endpoints for testing purposes.

### How to Use

1. Open **Insomnia**.
2. Go to **File → Import → From File**.
3. Select the file `insomnia/insomnia-collection.json` (path relative to this repo).
4. All endpoints and environment settings will be available in your workspace for testing.

> Note: No Insomnia account is needed to import and use this collection.
