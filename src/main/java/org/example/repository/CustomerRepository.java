package org.example.repository;

import org.example.model.Customer;

public interface CustomerRepository {
    void saveCustomer(Customer customer);
    Customer findByUsername(String username);

}
