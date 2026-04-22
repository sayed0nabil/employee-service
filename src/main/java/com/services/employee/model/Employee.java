package com.services.employee.model;

import java.time.LocalDate;

/**
 * Employee domain entity (MVC — Model layer).
 * Mutable class representing the stored employee data.
 */
public class Employee {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String department;
    private String position;
    private Double salary;
    private LocalDate hireDate;

    public Employee() {}

    public Employee(Long id,
                    String firstName,
                    String lastName,
                    String email,
                    String department,
                    String position,
                    Double salary,
                    LocalDate hireDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.department = department;
        this.position = position;
        this.salary = salary;
        this.hireDate = hireDate;
    }

    // ---- Getters & Setters ----

    public Long getId()                    { return id; }
    public void setId(Long id)             { this.id = id; }

    public String getFirstName()           { return firstName; }
    public void setFirstName(String v)     { this.firstName = v; }

    public String getLastName()            { return lastName; }
    public void setLastName(String v)      { this.lastName = v; }

    public String getEmail()               { return email; }
    public void setEmail(String v)         { this.email = v; }

    public String getDepartment()          { return department; }
    public void setDepartment(String v)    { this.department = v; }

    public String getPosition()            { return position; }
    public void setPosition(String v)      { this.position = v; }

    public Double getSalary()              { return salary; }
    public void setSalary(Double v)        { this.salary = v; }

    public LocalDate getHireDate()         { return hireDate; }
    public void setHireDate(LocalDate v)   { this.hireDate = v; }
}
