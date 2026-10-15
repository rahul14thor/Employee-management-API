package com.rahul.employeemanagement.employee;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EmployeeService {
    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Employee> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Employee findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    public Employee create(EmployeeRequest request) {
        String email = request.email().trim();
        if (repository.existsByEmailIgnoreCase(email)) {
            throw new DuplicateEmailException(email);
        }
        Employee employee = new Employee(
                request.firstName().trim(),
                request.lastName().trim(),
                email,
                request.department().trim(),
                request.jobTitle().trim()
        );
        return repository.save(employee);
    }

    public Employee update(Long id, EmployeeRequest request) {
        Employee employee = findById(id);
        String email = request.email().trim();
        repository.findByEmailIgnoreCase(email)
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> { throw new DuplicateEmailException(email); });

        employee.setFirstName(request.firstName().trim());
        employee.setLastName(request.lastName().trim());
        employee.setEmail(email);
        employee.setDepartment(request.department().trim());
        employee.setJobTitle(request.jobTitle().trim());
        return repository.save(employee);
    }

    public void delete(Long id) {
        Employee employee = findById(id);
        repository.delete(employee);
    }
}
