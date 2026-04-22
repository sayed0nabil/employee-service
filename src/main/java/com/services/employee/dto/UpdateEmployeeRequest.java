package com.services.employee.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

/**
 * Java Record DTO for updating an existing employee.
 * All fields are optional — only non-null values are applied.
 *
 * @param firstName  New first name (optional)
 * @param lastName   New last name (optional)
 * @param email      New email (optional, must be valid format)
 * @param department New department (optional)
 * @param position   New position (optional)
 * @param salary     New salary (optional, must be positive)
 */
@Schema(description = "Request payload for updating an existing employee")
public record UpdateEmployeeRequest(

        @Schema(description = "New first name", example = "Jane")
        @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
        String firstName,

        @Schema(description = "New last name", example = "Smith")
        @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
        String lastName,

        @Schema(description = "New email address", example = "jane.smith@company.com")
        @Email(message = "Email must be a valid email address")
        String email,

        @Schema(description = "New department", example = "Product")
        String department,

        @Schema(description = "New job position", example = "Tech Lead")
        String position,

        @Schema(description = "New monthly salary", example = "6500.00")
        @Positive(message = "Salary must be a positive value")
        Double salary
) {}
