package com.services.employee.service;

import com.services.employee.dto.CreateEmployeeRequest;
import com.services.employee.dto.EmployeeResponse;
import com.services.employee.dto.UpdateEmployeeRequest;

import java.util.List;

/**
 * Business logic contract for Employee operations.
 *
 * <p>Following the Dependency Inversion Principle, the controller depends on
 * this interface — not on any concrete implementation.</p>
 */
public interface EmployeeService {

    /**
     * Returns all employees in the system.
     *
     * @return list of employee response DTOs
     */
    List<EmployeeResponse> getAllEmployees();

    /**
     * Returns a single employee by their ID.
     *
     * @param id the employee's unique identifier
     * @return the matching employee response DTO
     * @throws com.services.employee.exception.EmployeeNotFoundException if not found
     */
    EmployeeResponse getEmployeeById(Long id);

    /**
     * Creates and persists a new employee.
     *
     * @param request the validated create request DTO
     * @return the newly created employee response DTO
     * @throws com.services.employee.exception.DuplicateEmailException if email is taken
     */
    EmployeeResponse createEmployee(CreateEmployeeRequest request);

    /**
     * Partially updates an existing employee. Only non-null fields are applied.
     *
     * @param id      the ID of the employee to update
     * @param request the update request DTO (fields may be null)
     * @return the updated employee response DTO
     * @throws com.services.employee.exception.EmployeeNotFoundException if not found
     * @throws com.services.employee.exception.DuplicateEmailException   if new email is taken
     */
    EmployeeResponse updateEmployee(Long id, UpdateEmployeeRequest request);

    /**
     * Deletes an employee by their ID.
     *
     * @param id the ID of the employee to delete
     * @throws com.services.employee.exception.EmployeeNotFoundException if not found
     */
    void deleteEmployee(Long id);
}
