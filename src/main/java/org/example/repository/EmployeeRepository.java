package org.example.repository;

import org.example.model.Employee;

public interface EmployeeRepository {
    // Shared
    Employee findByUsername(String username);
    // Manager Exclusive
    void saveEmployee(Employee employee);


}
