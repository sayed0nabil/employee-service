package com.services.employee.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

/**
 * Java Record DTO returned to the client for every employee resource.
 * Immutable view — no domain model internals exposed.
 *
 * @param id         Unique employee identifier
 * @param firstName  Employee's first name
 * @param lastName   Employee's last name
 * @param email      Corporate email
 * @param department Department name
 * @param position   Job title
 * @param salary     Monthly salary
 * @param hireDate   Date employee was hired
 */
@Schema(description = "Employee resource representation")
public record EmployeeResponse(

        @Schema(description = "Unique employee ID", example = "1")
        Long id,

        @Schema(description = "First name", example = "John")
        String firstName,

        @Schema(description = "Last name", example = "Doe")
        String lastName,

        @Schema(description = "Email address", example = "john.doe@company.com")
        String email,

        @Schema(description = "Department", example = "Engineering")
        String department,

        @Schema(description = "Job position", example = "Senior Developer")
        String position,

        @Schema(description = "Monthly salary", example = "5000.00")
        Double salary,

        @Schema(description = "Hire date", example = "2024-01-15")
        LocalDate hireDate
) {}
