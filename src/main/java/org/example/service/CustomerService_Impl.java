package org.example.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.example.exceptions.InvalidCredentialsException;
import org.example.exceptions.UserNotFoundException;
import org.example.model.Customer;
import org.example.repository.CustomerRepository;

public class CustomerService_Impl implements CustomerService{
    private CustomerRepository customerRepo = null;

    public CustomerService_Impl(CustomerRepository customerRepo){
        this.customerRepo = customerRepo;
    }

    @Override
    public void register(Customer customer) {
        // hashing plain-text password
        String hashString = BCrypt.withDefaults().hashToString(12, customer.getPassword().toCharArray());
        customer.setPassword(hashString);
        // pushing customer object to customerRepo;
        customerRepo.saveCustomer(customer);
    }

    @Override
    public Customer login(String username, String password) {
        Customer customer = customerRepo.findByUsername(username);
        if (customer != null) {
            BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), customer.getPassword());
            if (result.verified) {
                return customer;
            } else {
                throw new InvalidCredentialsException("Incorrect username or password");
            }
        } else {
            throw new UserNotFoundException("No customer found");
        }

    }
}

