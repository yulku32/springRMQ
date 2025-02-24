package com.example.paymentservice.config;

import com.example.paymentservice.model.Customer;
import com.example.paymentservice.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {
    private final CustomerRepository customerRepository;

    @Autowired
    public DataInitializer(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void run(String... args) {
        // Check if we already have customers
        if (customerRepository.count() == 0) {
            System.out.println("Initializing sample customers...");

            Customer alice = new Customer();
            alice.setName("Alice");
            alice.setBalance(1000.0);

            Customer bob = new Customer();
            bob.setName("Bob");
            bob.setBalance(500.0);

            customerRepository.saveAll(Arrays.asList(alice, bob));

            System.out.println("Sample data initialization completed.");
        } else {
            System.out.println("Customer data already exists, skipping initialization.");
        }
    }
}