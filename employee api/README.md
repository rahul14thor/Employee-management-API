# Employee Management API

A learning project for practicing Java, Spring Boot, REST API design, validation, and persistence. Build and understand each feature yourself before presenting this project as portfolio work.

## Tech stack

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA / Hibernate
- Bean Validation
- H2 in-memory database for local development
- PostgreSQL support through a separate profile
- Maven and JUnit 5

## Current starter features

- Create, list, retrieve, update, and delete employees
- Input validation for name, email, and department
- Duplicate email check
- Service layer between controller and repository
- Consistent handling for not-found, duplicate-email, and invalid-input errors
- H2 database so the API can run without installing PostgreSQL

## Run locally

Requirements: JDK 17+ and Maven 3.9+ (or use Maven Wrapper if you add it).

```bash
mvn spring-boot:run
```

The app runs at `http://localhost:8080`. H2 is in-memory by default, so its data resets when the app restarts.

## API endpoints

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/employees` | List employees |
| GET | `/api/employees/{id}` | Get one employee |
| POST | `/api/employees` | Create an employee |
| PUT | `/api/employees/{id}` | Update an employee |
| DELETE | `/api/employees/{id}` | Delete an employee |

### Example POST body

```json
{
  "firstName": "Ananya",
  "lastName": "Rao",
  "email": "ananya.rao@example.com",
  "department": "Engineering",
  "jobTitle": "Software Engineer"
}
```

Use Postman or curl to test the endpoints. The API returns JSON.

## Use PostgreSQL (optional next step)

Create a PostgreSQL database named `employee_db`, then set environment variables before starting with the `postgres` profile:

- `DB_URL` — for example `jdbc:postgresql://localhost:5432/employee_db`
- `DB_USERNAME`
- `DB_PASSWORD`

Run:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=postgres
```

Do not commit real passwords, tokens, or other secrets.

## Suggested roadmap

1. Understand every class and test the CRUD endpoints.
2. Add pagination and sorting to `GET /api/employees`.
3. Add search by department and name.
4. Write unit tests for the service and integration tests for the API.
5. Add department entities and relationships.
6. Add authentication/authorization only after the core API is stable.
7. Add a small Thymeleaf or plain HTML/CSS interface if you want a visible UI without React.
8. Add screenshots, sample requests, setup instructions, and test results to this README.

## Project status

This is a starter implementation, not a production-ready HR system. Complete the roadmap, verify the code locally, and update this README to describe only features you have personally implemented and tested.
