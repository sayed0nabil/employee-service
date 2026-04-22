package com.services.employee.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.employee.dto.auth.AuthResponse;
import com.services.employee.dto.auth.LoginRequest;
import com.services.employee.dto.auth.RegisterRequest;
import com.services.employee.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private com.services.employee.security.JwtAuthFilter jwtAuthFilter;

    @MockBean
    private com.services.employee.security.UserDetailsServiceImpl userDetailsService;

    @MockBean
    private com.services.employee.security.JwtService jwtService;

    @Test
    void register_ShouldReturn200AndAuthResponse() throws Exception {
        RegisterRequest request = new RegisterRequest("John Doe", "john@example.com", "password123");
        AuthResponse authResponse = new AuthResponse("mock-jwt-token");

        when(authService.register(any(RegisterRequest.class))).thenReturn(authResponse);

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock-jwt-token"))
                .andExpect(jsonPath("$.type").value("Bearer"));
    }

    @Test
    void register_ShouldReturn400_WhenValidationFails() throws Exception {
        RegisterRequest request = new RegisterRequest("", "invalid-email", "123"); // Invalid fields

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Validation Error"));
    }

    @Test
    void login_ShouldReturn200AndAuthResponse() throws Exception {
        LoginRequest request = new LoginRequest("john@example.com", "password123");
        AuthResponse authResponse = new AuthResponse("mock-jwt-token");

        when(authService.login(any(LoginRequest.class))).thenReturn(authResponse);

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock-jwt-token"));
    }
}
