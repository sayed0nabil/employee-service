package com.services.employee.repository.impl;

import com.services.employee.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryEmployeeRepositoryTest {

    private InMemoryEmployeeRepository employeeRepository;

    @BeforeEach
    void setUp() {
        employeeRepository = new InMemoryEmployeeRepository();
    }

    @Test
    void findAll_ShouldReturnInitialEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        assertFalse(employees.isEmpty());
        assertTrue(employees.size() >= 3); // Pre-seeded
    }

    @Test
    void findById_ShouldReturnEmployee_WhenExists() {
        Employee saved = employeeRepository.save(new Employee(null, "Test", "Test", "id-test@test.com", "IT", "Dev", 1000.0, java.time.LocalDate.now()));
        Optional<Employee> employee = employeeRepository.findById(saved.getId());
        assertTrue(employee.isPresent());
        assertEquals(saved.getId(), employee.get().getId());
    }

    @Test
    void findById_ShouldReturnEmpty_WhenNotExists() {
        Optional<Employee> employee = employeeRepository.findById(999L);
        assertTrue(employee.isEmpty());
    }

    @Test
    void save_ShouldAddNewEmployee() {
        Employee newEmployee = new Employee(null, "Test", "Test", "test@test.com", "IT", "Dev", 1000.0, java.time.LocalDate.now());
        Employee saved = employeeRepository.save(newEmployee);

        assertNotNull(saved.getId());
        assertEquals("Test", saved.getFirstName());
    }

    @Test
    void update_ShouldUpdateExistingEmployee_WhenExists() {
        Employee saved = employeeRepository.save(new Employee(null, "Original", "Name", "update-test@test.com", "IT", "Dev", 1000.0, java.time.LocalDate.now()));
        Employee updatedEmployee = new Employee(saved.getId(), "Updated", "Name", "updated@test.com", "HR", "Manager", 2000.0, java.time.LocalDate.now());
        Employee result = employeeRepository.update(updatedEmployee);

        assertNotNull(result);
        assertEquals("Updated", result.getFirstName());
    }

    @Test
    void update_ShouldReturnEmpty_WhenNotExists() {
        Employee updatedEmployee = new Employee(999L, "Updated", "Name", "updated@test.com", "HR", "Manager", 2000.0, java.time.LocalDate.now());
        Employee result = employeeRepository.update(updatedEmployee);

        assertNull(result);
    }

    @Test
    void deleteById_ShouldRemoveEmployee_WhenExists() {
        Employee saved = employeeRepository.save(new Employee(null, "Delete", "Me", "delete-test@test.com", "IT", "Dev", 1000.0, java.time.LocalDate.now()));
        boolean deleted = employeeRepository.deleteById(saved.getId());
        assertTrue(deleted);
        assertTrue(employeeRepository.findById(saved.getId()).isEmpty());
    }

    @Test
    void deleteById_ShouldReturnFalse_WhenNotExists() {
        boolean deleted = employeeRepository.deleteById(999L);
        assertFalse(deleted);
    }

    @Test
    void findByEmail_ShouldReturnEmployee_WhenExists() {
        employeeRepository.save(new Employee(null, "Email", "Test", "email-test@test.com", "IT", "Dev", 1000.0, java.time.LocalDate.now()));
        assertTrue(employeeRepository.findByEmail("email-test@test.com").isPresent());
    }

    @Test
    void findByEmail_ShouldReturnEmpty_WhenNotExists() {
        assertTrue(employeeRepository.findByEmail("nonexistent@example.com").isEmpty());
    }
}
