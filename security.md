# Spring Security & JWT Implementation Guide

This document explains the architecture and implementation of Spring Security and JSON Web Tokens (JWT) in the Employee Service.

## Overview

The application has been secured so that all API endpoints (specifically under `/api/v1/employees/**`) require a valid JWT token to be accessed. The only exceptions are the authentication endpoints (`/api/v1/auth/login` and `/api/v1/auth/register`) and the Swagger documentation.

## Components

### 1. Dependencies
We added `spring-boot-starter-security` for Spring Security and `jjwt-api`, `jjwt-impl`, `jjwt-jackson` to handle JWT generation, validation, and parsing.

### 2. User Management
- **`User` Model**: Implements Spring Security's `UserDetails` interface. This allows Spring to understand our user entity for authentication and authorization.
- **`UserRepository` & `InMemoryUserRepository`**: For demonstration, user accounts are stored in-memory using an `ArrayList`. Real-world applications should use a persistent database (e.g., PostgreSQL with Spring Data JPA).

### 3. JWT Service
`JwtService` is responsible for:
- **Generating Tokens**: Creates a signed token string containing the user's email as the subject, an issue date, and an expiration date (24 hours).
- **Validating Tokens**: Ensures the token is not expired and its signature matches.
- **Extracting Claims**: Parses the token to extract the username (email) and other information.

> **Warning**: The `SECRET` key in `JwtService` is currently hardcoded for demonstration purposes. In a production environment, this MUST be injected from a secure environment variable or secret manager.

### 4. JWT Authentication Filter
`JwtAuthFilter` extends `OncePerRequestFilter`. It intercepts every incoming HTTP request to check for the `Authorization: Bearer <token>` header.
1. If the header is missing or doesn't start with "Bearer ", the filter passes the request down the chain.
2. If a token is present, it extracts the username from it.
3. If the user is not currently authenticated in the `SecurityContext`, it loads the user details from the database (via `UserDetailsService`).
4. It validates the token against the user details.
5. If valid, it creates a `UsernamePasswordAuthenticationToken` and sets it in the `SecurityContextHolder`, effectively logging the user in for that specific request.

### 5. Security Configuration
`SecurityConfig` wires everything together:
- Disables CSRF (Cross-Site Request Forgery) since JWTs are not susceptible to typical CSRF attacks (they aren't automatically sent by the browser like session cookies).
- Configures session management to `STATELESS`, meaning Spring Security will not create HTTP sessions. Each request must be authenticated independently via the JWT.
- Exposes the `/api/v1/auth/**` and Swagger endpoints as `permitAll()`.
- Demands authentication (`anyRequest().authenticated()`) for everything else.
- Registers the `JwtAuthFilter` to run *before* the standard `UsernamePasswordAuthenticationFilter`.

### 6. Authentication Flow
**Registration**:
1. User sends a POST to `/api/v1/auth/register` with name, email, and password.
2. The `AuthService` verifies the email is not taken, encrypts the password using `BCryptPasswordEncoder`, and saves the user.
3. It then generates and returns a JWT token.

**Login**:
1. User sends a POST to `/api/v1/auth/login` with email and password.
2. `AuthService` uses Spring's `AuthenticationManager` to verify the credentials against the hashed password in the repository.
3. If successful, it generates and returns a new JWT token.

### 7. Swagger Integration
`OpenApiConfig` was updated to include a `SecurityScheme` of type `HTTP` and scheme `bearer`. This adds an "Authorize" button to the Swagger UI. When a token is provided there, Swagger automatically includes it as an `Authorization: Bearer <token>` header in all subsequent requests made via the UI.
