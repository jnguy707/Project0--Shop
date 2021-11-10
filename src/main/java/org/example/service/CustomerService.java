package org.example.service;

import org.example.model.Customer;
import org.example.model.Employee;
import org.example.repository.CustomerRepository;

public interface CustomerService {
    void register(Customer customer);
    Customer login(String username, String password);
}
