package com.rahul.employeemanagement.employee;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EmployeeRequest(
        @NotBlank @Size(max = 60) String firstName,
        @NotBlank @Size(max = 60) String lastName,
        @NotBlank @Email @Size(max = 150) String email,
        @NotBlank @Size(max = 80) String department,
        @NotBlank @Size(max = 100) String jobTitle
) {}
