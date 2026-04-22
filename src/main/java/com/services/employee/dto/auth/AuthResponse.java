package com.services.employee.dto.auth;

public record AuthResponse(
        String token,
        String type
) {
    public AuthResponse(String token) {
        this(token, "Bearer");
    }
}
