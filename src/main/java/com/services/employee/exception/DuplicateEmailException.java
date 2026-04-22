package com.services.employee.exception;

/**
 * Thrown when an attempt is made to register an email that already exists.
 */
public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException(String email) {
        super("An employee with email '" + email + "' already exists");
    }
}
