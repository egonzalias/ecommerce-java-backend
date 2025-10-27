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

AWS_REGION=us-east-1
AWS_URL=https://sqs.us-east-1.amazonaws.com
AWS_ACCOUNT_ID=
AWS_QUEUE_NAME=loan-request-status-updates
AWS_ACCESS_KEY_ID=
AWS_SECRET_ACCESS_KEY=
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
>

Swagger API Documentation

This project includes Swagger for API documentation and testing. You can easily explore and test all endpoints directly from a web interface.

Access Swagger UI:

Once the application is running, Swagger UI is available at:

http://localhost:8080/swagger-ui.html


Features:

Interactive documentation for all API endpoints

Automatic generation of request/response models

Test endpoints directly from the browser

Supports environment-specific configurations

Usage:

Start the backend using Docker Compose:

docker-compose up --build


Open your browser and navigate to http://localhost:8080/swagger-ui.html.

Explore endpoints, view models, and execute requests directly.

Notes:

Swagger uses the same base URL and port as the backend (http://localhost:8080).

All changes in your controllers or request/response models will automatically be reflected in Swagger UI
