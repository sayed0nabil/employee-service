package com.services.employee.mapper;

import com.services.employee.dto.CreateEmployeeRequest;
import com.services.employee.dto.EmployeeResponse;
import com.services.employee.model.Employee;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Mapper responsible for converting between Employee entity and DTOs.
 *
 * <p>Follows the Single Responsibility Principle — mapping is isolated
 * from both business logic and controller concerns.</p>
 */
@Component
public class EmployeeMapper {

    /**
     * Maps a {@link CreateEmployeeRequest} record to an {@link Employee} entity.
     * The ID is assigned later by the repository; hire date defaults to today.
     *
     * @param request the incoming create request DTO
     * @return a new, unpersisted Employee entity
     */
    public Employee toEntity(CreateEmployeeRequest request) {
        return new Employee(
                null,
                request.firstName(),
                request.lastName(),
                request.email(),
                request.department(),
                request.position(),
                request.salary(),
                LocalDate.now()
        );
    }

    /**
     * Maps an {@link Employee} entity to an {@link EmployeeResponse} record DTO.
     *
     * @param employee the stored employee entity
     * @return an immutable response DTO safe to serialize
     */
    public EmployeeResponse toResponse(Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getDepartment(),
                employee.getPosition(),
                employee.getSalary(),
                employee.getHireDate()
        );
    }
}
