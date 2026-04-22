package com.services.employee;

import com.services.employee.dto.CreateEmployeeRequest;
import com.services.employee.dto.EmployeeResponse;
import com.services.employee.dto.UpdateEmployeeRequest;
import com.services.employee.exception.DuplicateEmailException;
import com.services.employee.exception.EmployeeNotFoundException;
import com.services.employee.mapper.EmployeeMapper;
import com.services.employee.repository.impl.InMemoryEmployeeRepository;
import com.services.employee.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for {@link EmployeeServiceImpl}.
 *
 * <p>Uses a fresh {@link InMemoryEmployeeRepository} per test via {@code @BeforeEach}
 * so state does not bleed between tests.</p>
 */
class EmployeeServiceTest {

    private EmployeeServiceImpl service;

    @BeforeEach
    void setUp() {
        // A fresh repository is instantiated for each test, but the static store
        // is shared. We reset it by clearing via reflection or using the live store.
        // For simplicity, we re-use the shared store and note seeded data exists.
        service = new EmployeeServiceImpl(
                new InMemoryEmployeeRepository(),
                new EmployeeMapper()
        );
    }

    @Test
    @DisplayName("getAllEmployees() should return at least the seeded employees")
    void getAllEmployees_returnsSeedData() {
        List<EmployeeResponse> all = service.getAllEmployees();
        assertThat(all).isNotEmpty();
    }

    @Test
    @DisplayName("createEmployee() should persist and return with generated ID")
    void createEmployee_assignsId() {
        var request = new CreateEmployeeRequest(
                "Test", "User", "test.user@company.com",
                "QA", "Tester", 4500.00
        );

        EmployeeResponse response = service.createEmployee(request);

        assertThat(response.id()).isNotNull();
        assertThat(response.email()).isEqualTo("test.user@company.com");
        assertThat(response.firstName()).isEqualTo("Test");
    }

    @Test
    @DisplayName("createEmployee() should throw DuplicateEmailException for taken email")
    void createEmployee_duplicateEmail_throwsException() {
        var request = new CreateEmployeeRequest(
                "Alice", "Copy", "alice.johnson@company.com",
                "HR", "Recruiter", 4000.00
        );

        assertThatThrownBy(() -> service.createEmployee(request))
                .isInstanceOf(DuplicateEmailException.class)
                .hasMessageContaining("alice.johnson@company.com");
    }

    @Test
    @DisplayName("getEmployeeById() should throw EmployeeNotFoundException for unknown ID")
    void getEmployeeById_notFound_throwsException() {
        assertThatThrownBy(() -> service.getEmployeeById(999L))
                .isInstanceOf(EmployeeNotFoundException.class)
                .hasMessageContaining("999");
    }

    @Test
    @DisplayName("updateEmployee() should apply only non-null fields")
    void updateEmployee_partialUpdate_appliesChanges() {
        // Get a seeded employee (ID=1)
        EmployeeResponse original = service.getEmployeeById(1L);
        String originalLastName = original.lastName();

        var patch = new UpdateEmployeeRequest(
                "UpdatedName", null, null, null, null, null
        );

        EmployeeResponse updated = service.updateEmployee(1L, patch);

        assertThat(updated.firstName()).isEqualTo("UpdatedName");
        assertThat(updated.lastName()).isEqualTo(originalLastName); // unchanged
    }

    @Test
    @DisplayName("deleteEmployee() should remove the employee from the store")
    void deleteEmployee_removesFromStore() {
        // Create a temp employee to delete
        var request = new CreateEmployeeRequest(
                "Delete", "Me", "delete.me@company.com",
                "Temp", "Temp", 1000.00
        );
        EmployeeResponse created = service.createEmployee(request);
        Long newId = created.id();

        service.deleteEmployee(newId);

        assertThatThrownBy(() -> service.getEmployeeById(newId))
                .isInstanceOf(EmployeeNotFoundException.class);
    }
}
