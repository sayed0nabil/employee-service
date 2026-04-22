package com.services.employee.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

/**
 * Java Record DTO for creating a new employee.
 * Immutable by nature — leverages Java 16+ record feature.
 *
 * @param firstName  Employee's first name (required)
 * @param lastName   Employee's last name (required)
 * @param email      Unique corporate email (required, valid format)
 * @param department Department the employee belongs to
 * @param position   Job title / position
 * @param salary     Monthly salary (must be positive)
 */
@Schema(description = "Request payload for creating a new employee")
public record CreateEmployeeRequest(

        @Schema(description = "Employee first name", example = "John")
        @NotBlank(message = "First name is required")
        @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
        String firstName,

        @Schema(description = "Employee last name", example = "Doe")
        @NotBlank(message = "Last name is required")
        @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
        String lastName,

        @Schema(description = "Corporate email address", example = "john.doe@company.com")
        @NotBlank(message = "Email is required")
        @Email(message = "Email must be a valid email address")
        String email,

        @Schema(description = "Department name", example = "Engineering")
        @NotBlank(message = "Department is required")
        String department,

        @Schema(description = "Job position / title", example = "Senior Developer")
        @NotBlank(message = "Position is required")
        String position,

        @Schema(description = "Monthly salary in USD", example = "5000.00")
        @NotNull(message = "Salary is required")
        @Positive(message = "Salary must be a positive value")
        Double salary
) {}
