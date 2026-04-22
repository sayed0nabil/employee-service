package com.services.employee.repository;

import com.services.employee.model.Employee;

import java.util.List;
import java.util.Optional;

/**
 * Repository contract for Employee persistence operations.
 *
 * <p>Follows the Dependency Inversion Principle — higher-level modules
 * (service layer) depend on this abstraction, not on any concrete store.</p>
 */
public interface EmployeeRepository {

    /**
     * Returns all stored employees.
     *
     * @return an unmodifiable list of all employees
     */
    List<Employee> findAll();

    /**
     * Finds an employee by their unique ID.
     *
     * @param id the employee's ID
     * @return an {@link Optional} containing the employee if found
     */
    Optional<Employee> findById(Long id);

    /**
     * Finds an employee by their email address.
     *
     * @param email the email to search for
     * @return an {@link Optional} containing the employee if found
     */
    Optional<Employee> findByEmail(String email);

    /**
     * Persists a new employee and returns the saved entity with its generated ID.
     *
     * @param employee the employee to save (ID must be null)
     * @return the saved employee with an assigned ID
     */
    Employee save(Employee employee);

    /**
     * Replaces an existing employee record with the updated entity.
     *
     * @param employee the employee with updated data (ID must be set)
     * @return the updated employee entity
     */
    Employee update(Employee employee);

    /**
     * Removes an employee from the store by ID.
     *
     * @param id the ID of the employee to remove
     * @return {@code true} if the employee was found and removed, {@code false} otherwise
     */
    boolean deleteById(Long id);
}
