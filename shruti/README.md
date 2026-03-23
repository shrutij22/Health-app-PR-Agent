# User API — Spring Boot

A REST API for managing users, built with Spring Boot 3, JPA, and H2.

## Tech Stack

- Java 17
- Spring Boot 3.2
- Spring Data JPA
- H2 in-memory database
- Lombok
- Maven

---

## Getting Started

### Prerequisites
- Java 17+
- Maven 3.8+

### Run locally

```bash
git clone <your-repo-url>
cd user-api
mvn spring-boot:run
```

The server starts at `http://localhost:8080`

### Run tests

```bash
mvn test
```

---

## API Endpoints

### Health Check

| Method | Endpoint       | Description              |
|--------|----------------|--------------------------|
| GET    | `/api/health`  | Custom health check      |
| GET    | `/actuator/health` | Spring Actuator health |

**Response example:**
```json
{
  "status": "UP",
  "service": "user-api",
  "timestamp": "2024-03-17T10:00:00",
  "version": "1.0.0"
}
```

---

### User CRUD

| Method | Endpoint          | Description        |
|--------|-------------------|--------------------|
| GET    | `/api/users`      | List all users     |
| GET    | `/api/users/{id}` | Get user by ID     |
| POST   | `/api/users`      | Create a new user  |
| PUT    | `/api/users/{id}` | Update a user      |
| DELETE | `/api/users/{id}` | Delete a user      |

**Create user — request body:**
```json
{
  "name": "Alice Smith",
  "email": "alice@example.com"
}
```

**User response:**
```json
{
  "id": 1,
  "name": "Alice Smith",
  "email": "alice@example.com",
  "createdAt": "2024-03-17T10:00:00",
  "updatedAt": "2024-03-17T10:00:00"
}
```

**Update user — only include fields you want to change:**
```json
{
  "name": "Alice Johnson"
}
```

---

## Error Responses

All errors return a consistent JSON format:

```json
{
  "timestamp": "2024-03-17T10:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "User not found with id: 99"
}
```

Validation errors include a `details` map:

```json
{
  "timestamp": "2024-03-17T10:00:00",
  "status": 400,
  "error": "Validation failed",
  "details": {
    "email": "Email must be a valid address"
  }
}
```

---

## Database

Uses H2 in-memory for development. Access the H2 console at:

```
http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:userdb
Username: sa
Password: (leave blank)
```

To switch to MySQL/PostgreSQL, update `application.properties` with your datasource config and add the driver dependency to `pom.xml`.

---

## Project Structure

```
src/
├── main/java/com/demo/userapi/
│   ├── UserApiApplication.java       # Entry point
│   ├── controller/
│   │   ├── UserController.java       # CRUD endpoints
│   │   └── HealthController.java     # Health check
│   ├── service/
│   │   └── UserService.java          # Business logic
│   ├── repository/
│   │   └── UserRepository.java       # DB queries
│   ├── model/
│   │   └── User.java                 # JPA entity
│   ├── dto/
│   │   └── UserDto.java              # Request/Response DTOs
│   └── exception/
│       ├── UserNotFoundException.java
│       ├── DuplicateEmailException.java
│       └── GlobalExceptionHandler.java
└── test/java/com/demo/userapi/
    ├── UserServiceTest.java           # Unit tests
    └── UserControllerIntegrationTest.java  # Integration tests
```