package com.example.paymentservice.repository;

import com.example.paymentservice.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer, String> {
    Customer findByName(String name);
}
