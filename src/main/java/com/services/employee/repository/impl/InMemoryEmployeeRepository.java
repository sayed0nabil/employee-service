package com.services.employee.repository.impl;

import com.services.employee.model.Employee;
import com.services.employee.repository.EmployeeRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * In-memory implementation of {@link EmployeeRepository} backed by a static
 * {@link ArrayList}.
 *
 * <p>A thread-safe {@link AtomicLong} is used for ID generation to prevent
 * collisions under concurrent requests.</p>
 *
 * <p>Pre-seeded with sample employees so the API is immediately explorable
 * through Swagger UI without additional setup.</p>
 */
@Repository
public class InMemoryEmployeeRepository implements EmployeeRepository {

    /** Shared, in-memory data store. */
    private static final List<Employee> STORE = new ArrayList<>();

    /** Auto-increment ID counter. */
    private static final AtomicLong ID_SEQUENCE = new AtomicLong(1);

    // ---- Seed data -------------------------------------------------------
    static {
        STORE.add(new Employee(
                ID_SEQUENCE.getAndIncrement(),
                "Alice", "Johnson",
                "alice.johnson@company.com",
                "Engineering", "Software Engineer",
                6500.00, LocalDate.of(2022, 3, 15)
        ));
        STORE.add(new Employee(
                ID_SEQUENCE.getAndIncrement(),
                "Bob", "Williams",
                "bob.williams@company.com",
                "Product", "Product Manager",
                7200.00, LocalDate.of(2021, 7, 1)
        ));
        STORE.add(new Employee(
                ID_SEQUENCE.getAndIncrement(),
                "Carol", "Davis",
                "carol.davis@company.com",
                "Design", "UX Designer",
                5800.00, LocalDate.of(2023, 1, 20)
        ));
    }
    // -----------------------------------------------------------------------

    @Override
    public List<Employee> findAll() {
        return Collections.unmodifiableList(STORE);
    }

    @Override
    public Optional<Employee> findById(Long id) {
        return STORE.stream()
                .filter(emp -> emp.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<Employee> findByEmail(String email) {
        return STORE.stream()
                .filter(emp -> emp.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    @Override
    public Employee save(Employee employee) {
        employee.setId(ID_SEQUENCE.getAndIncrement());
        STORE.add(employee);
        return employee;
    }

    @Override
    public Employee update(Employee updated) {
        for (int i = 0; i < STORE.size(); i++) {
            if (STORE.get(i).getId().equals(updated.getId())) {
                STORE.set(i, updated);
                return updated;
            }
        }
        return null;
    }

    @Override
    public boolean deleteById(Long id) {
        return STORE.removeIf(emp -> emp.getId().equals(id));
    }
}
