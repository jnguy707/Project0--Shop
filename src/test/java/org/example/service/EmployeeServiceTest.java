package org.example.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.example.exceptions.InvalidCredentialsException;
import org.example.exceptions.UserNotFoundException;
import org.example.model.Employee;
import org.example.repository.EmployeeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class EmployeeServiceTest {
    private EmployeeService employeeService;
    private EmployeeRepository employeeRepo;
    private Employee employee;

    @BeforeEach
    public void setup() {
        employee = Mockito.mock(Employee.class);
        employeeRepo = Mockito.mock(EmployeeRepository.class);
        employeeService = new EmployeeService_Impl(employeeRepo);


    }

    @Test
    public void registerTest(){
        // when method call because customer is being mocked
        Mockito.when(employee.getPassword()).thenReturn("pass1");
        // invoke
        employeeService.register(employee);

        // Assert
        Mockito.verify(employeeRepo, Mockito.times(1))
                .saveEmployee(employee);

    }

    @Test
    public void validLoginTest(){
        String pass = "pass1";

        // Expected
        Mockito.when(employee.getUsername()).thenReturn("user1");
        Mockito.when(employee.getPassword()).thenReturn(
                BCrypt.withDefaults().hashToString(12, pass.toCharArray())
        );
        Mockito.when(employeeRepo.findByUsername(employee.getUsername())).thenReturn(employee);
        Employee actualEmployee = employeeService.login("user1", pass);
        // Assert
        Assertions.assertEquals(employee,actualEmployee);
    }

    @Test
    public void invalidCredentialsLoginTest() {
        String pass = "pass1";
        // Expected
        Mockito.when(employee.getUsername()).thenReturn("user1");
        Mockito.when(employee.getPassword()).thenReturn(
                BCrypt.withDefaults().hashToString(12, pass.toCharArray())
        );
        Mockito.when(employeeRepo.findByUsername(employee.getUsername())).thenReturn(employee);
        // Actual & Assert
        Assertions.assertThrows(InvalidCredentialsException.class, () ->
                employeeService.login("user1","pass2"));
    }

    @Test
    public void userNotFoundLoginTest(){
        String pass = "pass1";
        // Expected
        Mockito.when(employee.getUsername()).thenReturn("user1");
        Mockito.when(employee.getPassword()).thenReturn(
                BCrypt.withDefaults().hashToString(12, pass.toCharArray())
        );
        Mockito.when(employeeRepo.findByUsername(employee.getUsername())).thenReturn(employee);
        // Actual & Assert
        Assertions.assertThrows(UserNotFoundException.class, () ->
                employeeService.login("user2","pass1"));
    }
}
