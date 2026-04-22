package com.services.employee.service.impl;

import com.services.employee.dto.CreateEmployeeRequest;
import com.services.employee.dto.EmployeeResponse;
import com.services.employee.dto.UpdateEmployeeRequest;
import com.services.employee.exception.DuplicateEmailException;
import com.services.employee.exception.EmployeeNotFoundException;
import com.services.employee.mapper.EmployeeMapper;
import com.services.employee.model.Employee;
import com.services.employee.repository.EmployeeRepository;
import com.services.employee.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Concrete implementation of {@link EmployeeService}.
 *
 * <p>Responsibilities (Single Responsibility Principle):</p>
 * <ul>
 *   <li>Orchestrate calls to the repository</li>
 *   <li>Apply business rules (e.g. duplicate email check)</li>
 *   <li>Delegate mapping to {@link EmployeeMapper}</li>
 * </ul>
 *
 * <p>This class is NOT aware of HTTP — it works purely with domain objects and DTOs.</p>
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    /** Constructor injection — preferred over field injection for testability. */
    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                                EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    // -------------------------------------------------------------------------
    // Read operations
    // -------------------------------------------------------------------------

    @Override
    public List<EmployeeResponse> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(employeeMapper::toResponse)
                .toList();
    }

    @Override
    public EmployeeResponse getEmployeeById(Long id) {
        Employee employee = findOrThrow(id);
        return employeeMapper.toResponse(employee);
    }

    // -------------------------------------------------------------------------
    // Write operations
    // -------------------------------------------------------------------------

    @Override
    public EmployeeResponse createEmployee(CreateEmployeeRequest request) {
        assertEmailIsUnique(request.email(), null);

        Employee employee = employeeMapper.toEntity(request);
        Employee saved = employeeRepository.save(employee);
        return employeeMapper.toResponse(saved);
    }

    @Override
    public EmployeeResponse updateEmployee(Long id, UpdateEmployeeRequest request) {
        Employee existing = findOrThrow(id);

        // Email uniqueness check only when a new email is being set
        if (request.email() != null && !request.email().equalsIgnoreCase(existing.getEmail())) {
            assertEmailIsUnique(request.email(), id);
        }

        applyPatch(existing, request);
        Employee updated = employeeRepository.update(existing);
        return employeeMapper.toResponse(updated);
    }

    @Override
    public void deleteEmployee(Long id) {
        // Verify employee exists before attempting deletion
        findOrThrow(id);
        employeeRepository.deleteById(id);
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    /**
     * Fetches an employee or throws {@link EmployeeNotFoundException}.
     */
    private Employee findOrThrow(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    /**
     * Ensures that no other employee (excluding {@code excludeId}) owns the given email.
     */
    private void assertEmailIsUnique(String email, Long excludeId) {
        employeeRepository.findByEmail(email).ifPresent(existing -> {
            if (!existing.getId().equals(excludeId)) {
                throw new DuplicateEmailException(email);
            }
        });
    }

    /**
     * Applies non-null fields from {@link UpdateEmployeeRequest} onto the existing entity.
     * Null fields are intentionally left unchanged (partial update / PATCH semantics).
     */
    private void applyPatch(Employee target, UpdateEmployeeRequest patch) {
        if (patch.firstName()  != null) target.setFirstName(patch.firstName());
        if (patch.lastName()   != null) target.setLastName(patch.lastName());
        if (patch.email()      != null) target.setEmail(patch.email());
        if (patch.department() != null) target.setDepartment(patch.department());
        if (patch.position()   != null) target.setPosition(patch.position());
        if (patch.salary()     != null) target.setSalary(patch.salary());
    }
}
