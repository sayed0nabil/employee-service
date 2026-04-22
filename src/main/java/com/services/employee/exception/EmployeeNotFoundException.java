package com.services.employee.exception;

/**
 * Thrown when an employee with the requested ID does not exist in the store.
 */
public class EmployeeNotFoundException extends RuntimeException {

    public EmployeeNotFoundException(Long id) {
        super("Employee not found with id: " + id);
    }
}
