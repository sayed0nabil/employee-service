package com.services.employee.service;

import com.services.employee.dto.auth.AuthResponse;
import com.services.employee.dto.auth.LoginRequest;
import com.services.employee.dto.auth.RegisterRequest;
import com.services.employee.exception.DuplicateEmailException;
import com.services.employee.model.User;
import com.services.employee.repository.UserRepository;
import com.services.employee.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @Test
    void register_ShouldReturnAuthResponse_WhenEmailIsUnique() {
        RegisterRequest request = new RegisterRequest("John Doe", "john@example.com", "password");
        User user = new User("John Doe", "john@example.com", "encodedPassword", "ROLE_USER");
        
        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(passwordEncoder.encode(request.password())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn("jwt-token");

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("jwt-token", response.token());
        assertEquals("Bearer", response.type());
    }

    @Test
    void register_ShouldThrowException_WhenEmailExists() {
        RegisterRequest request = new RegisterRequest("John Doe", "john@example.com", "password");
        when(userRepository.existsByEmail(request.email())).thenReturn(true);

        assertThrows(DuplicateEmailException.class, () -> authService.register(request));
    }

    @Test
    void login_ShouldReturnAuthResponse_WhenCredentialsAreValid() {
        LoginRequest request = new LoginRequest("john@example.com", "password");
        User user = new User("John Doe", "john@example.com", "encodedPassword", "ROLE_USER");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("jwt-token");

        AuthResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("jwt-token", response.token());
    }
}
