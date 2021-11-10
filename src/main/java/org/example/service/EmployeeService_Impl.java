package org.example.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.example.exceptions.InvalidCredentialsException;
import org.example.exceptions.UserNotFoundException;
import org.example.model.Employee;
import org.example.repository.EmployeeRepository;

public class EmployeeService_Impl implements EmployeeService{
    private EmployeeRepository employeeRepo;

    public EmployeeService_Impl(EmployeeRepository employeeRepo){
        this.employeeRepo =  employeeRepo;
    }
    @Override
    public void register(Employee employee) {
        // hash plain-text password ;
        String hashString = BCrypt.withDefaults().hashToString(12, employee.getPassword().toCharArray());
        employee.setPassword(hashString);
        // pass employee to employeeRepo.save()
        employeeRepo.saveEmployee(employee);
    }

    @Override
    public Employee login(String username, String password) {
        username = username.toLowerCase();
        Employee employee = employeeRepo.findByUsername(username);
        if (employee != null) {
            BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), employee.getPassword());
            if (result.verified) {
                return employee;
            } else {
                throw new InvalidCredentialsException("Incorrect username or password");
            }
        } else {
            throw new UserNotFoundException("No employee found");
        }
    }
}
