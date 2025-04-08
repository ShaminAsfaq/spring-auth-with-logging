
# Spring Boot JWT Authentication & Authorization Template

This is a starter Spring Boot project template for building secure backend services using JWT (JSON Web Tokens) for authentication and authorization. It includes:

- ✅ User Registration & Login
- ✅ JWT-based Authentication
- ✅ Role-based Authorization with @PreAuthorize
- ✅ Swagger (OpenAPI) integration for API documentation
- ✅ DTOs and Mappers using MapStruct
- ✅ Exception handling structure
- ✅ Spring Security configuration

---

## 🔧 Tech Stack

- Java 17+
- Spring Boot
- Spring Security
- JWT
- Swagger (Springdoc OpenAPI)
- MapStruct
- Lombok
- H2 (quicker to set up and test)
- Maven

---

## 🔐 Authentication & Authorization

- Users authenticate using `/api/auth/login`.
- JWT is returned in the response and must be included in the `Authorization` header as `Bearer <token>`.
- You can use `@PreAuthorize("hasRole('ADMIN')")` on endpoints to protect them.

---

## 🛠️ How to Use

### 1. Fork the Project

```bash
git clone https://github.com/YOUR_USERNAME/YOUR_REPO.git
cd YOUR_REPO
```

### 2. Add Your Custom Code

Start building your business logic on top of the structure.

---
## API Endpoints

---

## 🧪 Testing the API

Use Swagger UI: http://localhost:8080/swagger-ui/index.html

---

### 1. Registration

- **POST** `/api/auth/register`
- **Description**: Registers a new user with a username and password.
- **Request**:
  ```json
  {
    "username": "newuser",
    "password": "password123"
  }
  ```
- **Response**:
    - **201 Created**: Returns the created user object.

### 2. Login

- **POST** `/api/auth/login`
- **Description**: Authenticates a user and generates a JWT token.
- **Request**:
  ```json
  {
    "username": "newuser",
    "password": "password123"
  }
  ```
- **Response**:
  ```json
  {
    "tokenType": "Bearer",
    "accessToken": "JWT_TOKEN",
    "expiresIn": 3600
  }
  ```

---

## Security Configuration

- **JWT Authentication**: The system uses JWT tokens to authenticate users. After logging in, the user receives a token that must be passed in the `Authorization` header for protected routes.
- **Roles**: There are two roles configured in this template:
    - `ROLE_USER`: Regular users.
    - `ROLE_ADMIN`: Admin users who have additional privileges.

---

## JWT Token

### How JWT works:
1. **Token Generation**: Once the user logs in, a JWT token is generated containing user details and roles.
2. **Token Validation**: For each subsequent request, the server checks if the JWT is valid and not expired.
3. **Token Expiration**: Tokens expire after a set duration. You can refresh tokens as needed.

---

## 📌 Best Practices (Recommended)

### ✅ 1. Role Management

- Create `RoleEntity` and `RoleRepository`.

### ✅ 2. Token Expiry & Refresh Flow

- JWT tokens expire after a period.
- Recommended: Add `/refresh-token` endpoint using a separate refresh token.

### ✅ 3. Audit Fields

Use:

```java
@CreationTimestamp
private LocalDateTime createdAt;

@UpdateTimestamp
private LocalDateTime updatedAt;
```

### ✅ 4. Testing

Add JUnit & Spring Boot test cases using `@WebMvcTest` or `@SpringBootTest`.

### ✅ 5. Rate Limiting

Use libraries like Bucket4J to prevent brute-force on `/login`.

### ✅ 6. CORS Configuration

Configure CORS properly in production environment instead of disabling it.

### ✅ 7. API Versioning

Namespace your APIs:

```
/api/v1/auth/login
```

---

## 📂 Project Structure

```
src/
├── auth/                  # Auth related logic (JWT, filters)
├── config/                # Spring Security configuration
├── controller/            # API endpoints
├── model/                 # Class/Relevant Files
    ├── dto/               # Data Transfer Objects
    ├── entity/            # JPA Entities
    ├── mapper/            # MapStruct mappers
    ├── enums/             # Enumurations
├── exception/             # Custom exceptions
├── repository/            # Spring Data JPA Repos
├── service/               # Business logic
└── swagger/               # Swagger OpenAPI config
```

---

## 📃 License

This template is open for modification and extension. Fork it and build your backend on top of it.

---

## 🏁 Next Steps

- Add real-world business logic
- Connect with frontend (Angular/React)
- Add token refresh & logout
- Add MySQL/PostgreSQL in prod

Happy coding! 🚀
