package com.services.employee.security;

import com.services.employee.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private User user;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        user = new User("John Doe", "john@example.com", "password", "ROLE_USER");
    }

    @Test
    void generateToken_ShouldReturnValidToken() {
        String token = jwtService.generateToken(user);
        
        assertNotNull(token);
        assertFalse(token.isEmpty());
        
        String extractedUsername = jwtService.extractUsername(token);
        assertEquals(user.getUsername(), extractedUsername);
    }

    @Test
    void isTokenValid_ShouldReturnTrue_ForValidTokenAndUser() {
        String token = jwtService.generateToken(user);
        
        boolean isValid = jwtService.isTokenValid(token, user);
        
        assertTrue(isValid);
    }

    @Test
    void isTokenValid_ShouldReturnFalse_ForDifferentUser() {
        String token = jwtService.generateToken(user);
        User differentUser = new User("Jane Doe", "jane@example.com", "password", "ROLE_USER");
        
        boolean isValid = jwtService.isTokenValid(token, differentUser);
        
        assertFalse(isValid);
    }
}
