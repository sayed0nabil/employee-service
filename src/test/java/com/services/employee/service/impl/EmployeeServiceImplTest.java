package com.services.employee.service.impl;

import com.services.employee.dto.CreateEmployeeRequest;
import com.services.employee.dto.EmployeeResponse;
import com.services.employee.dto.UpdateEmployeeRequest;
import com.services.employee.exception.DuplicateEmailException;
import com.services.employee.exception.EmployeeNotFoundException;
import com.services.employee.mapper.EmployeeMapper;
import com.services.employee.model.Employee;
import com.services.employee.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;
    private EmployeeResponse employeeResponse;

    @BeforeEach
    void setUp() {
        employee = new Employee(1L, "John", "Doe", "john@example.com", "IT", "Dev", 5000.0, java.time.LocalDate.now());
        employeeResponse = new EmployeeResponse(1L, "John", "Doe", "john@example.com", "IT", "Dev", 5000.0, java.time.LocalDate.now());
    }

    @Test
    void getAllEmployees_ShouldReturnListOfEmployees() {
        when(employeeRepository.findAll()).thenReturn(List.of(employee));
        when(employeeMapper.toResponse(employee)).thenReturn(employeeResponse);

        List<EmployeeResponse> responses = employeeService.getAllEmployees();

        assertFalse(responses.isEmpty());
        assertEquals(1, responses.size());
        assertEquals("John", responses.get(0).firstName());
    }

    @Test
    void getEmployeeById_ShouldReturnEmployee_WhenExists() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeMapper.toResponse(employee)).thenReturn(employeeResponse);

        EmployeeResponse response = employeeService.getEmployeeById(1L);

        assertNotNull(response);
        assertEquals(1L, response.id());
    }

    @Test
    void getEmployeeById_ShouldThrowException_WhenNotExists() {
        when(employeeRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.getEmployeeById(999L));
    }

    @Test
    void createEmployee_ShouldReturnEmployee_WhenEmailIsUnique() {
        CreateEmployeeRequest request = new CreateEmployeeRequest("John", "Doe", "new@example.com", "IT", "Dev", 5000.0);
        Employee mappedEmployee = new Employee(null, "John", "Doe", "new@example.com", "IT", "Dev", 5000.0, java.time.LocalDate.now());

        when(employeeRepository.findByEmail(request.email())).thenReturn(Optional.empty());
        when(employeeMapper.toEntity(request)).thenReturn(mappedEmployee);
        when(employeeRepository.save(mappedEmployee)).thenReturn(employee);
        when(employeeMapper.toResponse(employee)).thenReturn(employeeResponse);

        EmployeeResponse response = employeeService.createEmployee(request);

        assertNotNull(response);
        assertEquals(1L, response.id());
    }

    @Test
    void createEmployee_ShouldThrowException_WhenEmailExists() {
        CreateEmployeeRequest request = new CreateEmployeeRequest("John", "Doe", "existing@example.com", "IT", "Dev", 5000.0);
        when(employeeRepository.findByEmail(request.email())).thenReturn(Optional.of(employee)); // employee has id 1

        assertThrows(DuplicateEmailException.class, () -> employeeService.createEmployee(request));
    }

    @Test
    void updateEmployee_ShouldReturnUpdatedEmployee_WhenExistsAndEmailIsUnique() {
        UpdateEmployeeRequest request = new UpdateEmployeeRequest("Johnny", "Doe", "johnny@example.com", "IT", "Dev", 6000.0);
        
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.findByEmail(request.email())).thenReturn(Optional.empty());
        when(employeeRepository.update(any(Employee.class))).thenReturn(employee);
        when(employeeMapper.toResponse(employee)).thenReturn(employeeResponse);

        EmployeeResponse response = employeeService.updateEmployee(1L, request);

        assertNotNull(response);
    }

    @Test
    void updateEmployee_ShouldThrowException_WhenEmailExistsForOther() {
        UpdateEmployeeRequest request = new UpdateEmployeeRequest("Johnny", "Doe", "other@example.com", "IT", "Dev", 6000.0);
        Employee otherEmployee = new Employee(2L, "Jane", "Doe", "other@example.com", "IT", "Dev", 6000.0, java.time.LocalDate.now());
        
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.findByEmail(request.email())).thenReturn(Optional.of(otherEmployee));

        assertThrows(DuplicateEmailException.class, () -> employeeService.updateEmployee(1L, request));
    }

    @Test
    void updateEmployee_ShouldThrowException_WhenNotExists() {
        UpdateEmployeeRequest request = new UpdateEmployeeRequest("Johnny", "Doe", "johnny@example.com", "IT", "Dev", 6000.0);
        when(employeeRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.updateEmployee(999L, request));
    }

    @Test
    void deleteEmployee_ShouldDoNothing_WhenExists() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.deleteById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> employeeService.deleteEmployee(1L));
        verify(employeeRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteEmployee_ShouldThrowException_WhenNotExists() {
        when(employeeRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.deleteEmployee(999L));
    }
}
