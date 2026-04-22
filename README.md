# Employee Service

A **Spring Boot 3.4 / Java 21** REST API for Employee CRUD operations with an in-memory `ArrayList` data store, Swagger UI, and Docker support.

---

## Tech Stack

| Technology | Version |
|------------|---------|
| Java | 21 (records, text blocks, sealed classes) |
| Spring Boot | 3.4.4 |
| SpringDoc OpenAPI | 2.8.6 (Swagger UI) |
| Maven | 3.x |
| Docker | Multi-stage build |

---

## Architecture (MVC + SOLID)

```
controller/          ← HTTP layer only (SRP)
service/             ← Business logic interface (DIP)
service/impl/        ← Concrete business logic (LSP)
repository/          ← Data access interface (DIP, ISP)
repository/impl/     ← In-memory ArrayList store
model/               ← Domain entity
dto/                 ← Java Records (immutable DTOs)
mapper/              ← Entity ↔ DTO conversion (SRP)
exception/           ← Custom exceptions + GlobalExceptionHandler (OCP)
config/              ← OpenAPI / Swagger configuration
```

---

## Running Locally

### Option 1 — Maven

```bash
./mvnw spring-boot:run
```

### Option 2 — Docker

```bash
# Build the image
docker build -t employee-service .

# Run on port 3000
docker run -p 3000:3000 --name employee-service employee-service
```

---

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/v1/employees` | List all employees |
| `GET` | `/api/v1/employees/{id}` | Get employee by ID |
| `POST` | `/api/v1/employees` | Create new employee |
| `PUT` | `/api/v1/employees/{id}` | Update employee (partial) |
| `DELETE` | `/api/v1/employees/{id}` | Delete employee |

---

## Swagger UI

Once running, open: **http://localhost:3000/swagger-ui.html**

Raw OpenAPI JSON: **http://localhost:3000/api-docs**

---

## Sample Create Request

```json
POST /api/v1/employees
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@company.com",
  "department": "Engineering",
  "position": "Senior Developer",
  "salary": 7000.00
}
```

---

## Running Tests

```bash
mvn test
```
