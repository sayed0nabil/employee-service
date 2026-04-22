package com.services.employee.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.services.employee.dto.CreateEmployeeRequest;
import com.services.employee.dto.EmployeeResponse;
import com.services.employee.dto.UpdateEmployeeRequest;
import com.services.employee.exception.EmployeeNotFoundException;
import com.services.employee.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
@AutoConfigureMockMvc(addFilters = false)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeService employeeService;

    @MockBean
    private com.services.employee.security.JwtAuthFilter jwtAuthFilter;

    @MockBean
    private com.services.employee.security.UserDetailsServiceImpl userDetailsService;

    @MockBean
    private com.services.employee.security.JwtService jwtService;

    @Test
    void getAllEmployees_ShouldReturn200AndList() throws Exception {
        EmployeeResponse response = new EmployeeResponse(1L, "John", "Doe", "john@example.com", "IT", "Dev", 5000.0, java.time.LocalDate.now());
        when(employeeService.getAllEmployees()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].firstName").value("John"));
    }

    @Test
    void getEmployeeById_ShouldReturn200AndEmployee_WhenExists() throws Exception {
        EmployeeResponse response = new EmployeeResponse(1L, "John", "Doe", "john@example.com", "IT", "Dev", 5000.0, java.time.LocalDate.now());
        when(employeeService.getEmployeeById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getEmployeeById_ShouldReturn404_WhenNotExists() throws Exception {
        when(employeeService.getEmployeeById(999L)).thenThrow(new EmployeeNotFoundException(999L));

        mockMvc.perform(get("/api/v1/employees/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Employee Not Found"));
    }

    @Test
    void createEmployee_ShouldReturn201AndEmployee() throws Exception {
        CreateEmployeeRequest request = new CreateEmployeeRequest("John", "Doe", "john@example.com", "IT", "Dev", 5000.0);
        EmployeeResponse response = new EmployeeResponse(1L, "John", "Doe", "john@example.com", "IT", "Dev", 5000.0, java.time.LocalDate.now());

        when(employeeService.createEmployee(any(CreateEmployeeRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void createEmployee_ShouldReturn400_WhenValidationFails() throws Exception {
        CreateEmployeeRequest request = new CreateEmployeeRequest("", "Doe", "invalid", "IT", "Dev", -10.0);

        mockMvc.perform(post("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Validation Error"));
    }

    @Test
    void updateEmployee_ShouldReturn200AndEmployee() throws Exception {
        UpdateEmployeeRequest request = new UpdateEmployeeRequest("John", "Doe", "john@example.com", "IT", "Dev", 6000.0);
        EmployeeResponse response = new EmployeeResponse(1L, "John", "Doe", "john@example.com", "IT", "Dev", 6000.0, java.time.LocalDate.now());

        when(employeeService.updateEmployee(eq(1L), any(UpdateEmployeeRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/employees/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.salary").value(6000.0));
    }

    @Test
    void deleteEmployee_ShouldReturn204() throws Exception {
        doNothing().when(employeeService).deleteEmployee(1L);

        mockMvc.perform(delete("/api/v1/employees/1"))
                .andExpect(status().isNoContent());
    }
}
