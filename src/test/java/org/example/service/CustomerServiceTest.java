package org.example.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.example.exceptions.InvalidCredentialsException;
import org.example.exceptions.UserNotFoundException;
import org.example.model.Customer;

import org.example.repository.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mock;
import org.mockito.Mockito;

public class CustomerServiceTest {

    private CustomerService customerService;
    private CustomerRepository customerRepo;
    private Customer customer;

    @BeforeEach
    public void setup() {
        customer = Mockito.mock(Customer.class);
        customerRepo = Mockito.mock(CustomerRepository.class);
        customerService = new CustomerService_Impl(customerRepo);


    }

    @Test
    public void registerTest(){
        // when method call because customer is being mocked
        Mockito.when(customer.getPassword()).thenReturn("pass1");
        // invoke
        customerService.register(customer);

        // Assert
        Mockito.verify(customerRepo, Mockito.times(1))
                .saveCustomer(customer);

    }

    @Test
    public void validLoginTest(){
        String pass = "pass1";

        // Expected
        Mockito.when(customer.getUsername()).thenReturn("user1");
        Mockito.when(customer.getPassword()).thenReturn(
        BCrypt.withDefaults().hashToString(12, pass.toCharArray())
        );
        Mockito.when(customerRepo.findByUsername(customer.getUsername())).thenReturn(customer);
        // Actual
        Customer actualCustomer = customerService.login("user1", pass);
        // Assert
        Assertions.assertEquals(customer,actualCustomer);
    }

    @Test
    public void invalidCredentialsLoginTest() {
        String pass = "pass1";
        // Expected
        Mockito.when(customer.getUsername()).thenReturn("user1");
        Mockito.when(customer.getPassword()).thenReturn(
                BCrypt.withDefaults().hashToString(12, pass.toCharArray())
        );
        Mockito.when(customerRepo.findByUsername(customer.getUsername())).thenReturn(customer);
        // Actual & Assert
        Assertions.assertThrows(InvalidCredentialsException.class, () ->
                customerService.login("user1","pass2"));
    }

    @Test
    public void userNotFoundLoginTest(){
        String pass = "pass1";
        // Expected
        Mockito.when(customer.getUsername()).thenReturn("user1");
        Mockito.when(customer.getPassword()).thenReturn(
                BCrypt.withDefaults().hashToString(12, pass.toCharArray())
        );
        Mockito.when(customerRepo.findByUsername(customer.getUsername())).thenReturn(customer);
        // Actual & Assert
        Assertions.assertThrows(UserNotFoundException.class, () ->
                customerService.login("user2","pass1"));
    }
}

