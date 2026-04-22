package com.services.employee.controller;

import com.services.employee.dto.CreateEmployeeRequest;
import com.services.employee.dto.EmployeeResponse;
import com.services.employee.dto.UpdateEmployeeRequest;
import com.services.employee.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Employee CRUD operations (MVC — Controller layer).
 *
 * <p>Responsibilities (Single Responsibility Principle):</p>
 * <ul>
 *   <li>Accept HTTP requests and validate input via {@code @Valid}</li>
 *   <li>Delegate ALL business logic to {@link EmployeeService}</li>
 *   <li>Return appropriate HTTP status codes and response bodies</li>
 * </ul>
 *
 * <p>Base path: {@code /api/v1/employees}</p>
 */
@RestController
@RequestMapping("/api/v1/employees")
@Tag(name = "Employee", description = "CRUD operations for managing employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    /** Constructor injection keeps this class easily testable (DIP). */
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // =========================================================================
    // GET /api/v1/employees
    // =========================================================================

    @GetMapping
    @Operation(
            summary = "Get all employees",
            description = "Returns the complete list of employees stored in memory."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved list of employees",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = EmployeeResponse.class))
    )
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    // =========================================================================
    // GET /api/v1/employees/{id}
    // =========================================================================

    @GetMapping("/{id}")
    @Operation(
            summary = "Get employee by ID",
            description = "Returns a single employee matching the provided ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employee found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeResponse.class))),
            @ApiResponse(responseCode = "404", description = "Employee not found",
                    content = @Content(mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<EmployeeResponse> getEmployeeById(
            @Parameter(description = "Unique employee ID", example = "1", required = true)
            @PathVariable Long id) {

        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    // =========================================================================
    // POST /api/v1/employees
    // =========================================================================

    @PostMapping
    @Operation(
            summary = "Create a new employee",
            description = "Creates and persists a new employee. Email must be unique."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Employee created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed",
                    content = @Content(mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "409", description = "Email already in use",
                    content = @Content(mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<EmployeeResponse> createEmployee(
            @Valid @RequestBody CreateEmployeeRequest request) {

        EmployeeResponse created = employeeService.createEmployee(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // =========================================================================
    // PUT /api/v1/employees/{id}
    // =========================================================================

    @PutMapping("/{id}")
    @Operation(
            summary = "Update an existing employee",
            description = """
                    Updates the employee with the given ID.
                    Only fields provided in the request body are updated (partial update semantics).
                    Omitted or null fields retain their existing values.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employee updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed",
                    content = @Content(mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Employee not found",
                    content = @Content(mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "409", description = "Email already in use",
                    content = @Content(mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @Parameter(description = "ID of the employee to update", example = "1", required = true)
            @PathVariable Long id,
            @Valid @RequestBody UpdateEmployeeRequest request) {

        return ResponseEntity.ok(employeeService.updateEmployee(id, request));
    }

    // =========================================================================
    // DELETE /api/v1/employees/{id}
    // =========================================================================

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete an employee",
            description = "Permanently removes the employee with the given ID from the store."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Employee deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Employee not found",
                    content = @Content(mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<Void> deleteEmployee(
            @Parameter(description = "ID of the employee to delete", example = "1", required = true)
            @PathVariable Long id) {

        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}
