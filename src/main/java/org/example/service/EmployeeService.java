package org.example.service;

import org.example.model.Employee;

public interface EmployeeService {
    void register(Employee employee);
    Employee login(String username, String password);
}
