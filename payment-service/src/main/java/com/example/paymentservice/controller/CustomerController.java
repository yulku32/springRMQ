package com.example.paymentservice.controller;

import com.example.paymentservice.model.Customer;
import com.example.paymentservice.model.Payment;
import com.example.paymentservice.repository.CustomerRepository;
import com.example.paymentservice.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerRepository customerRepository;
    private final PaymentRepository paymentRepository;

    @Autowired
    public CustomerController(CustomerRepository customerRepository,
                              PaymentRepository paymentRepository) {
        this.customerRepository = customerRepository;
        this.paymentRepository = paymentRepository;
    }

    @PostMapping
    public Customer createCustomer(@RequestBody Customer customer) {
        return customerRepository.save(customer);
    }

    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @GetMapping("/{name}")
    public ResponseEntity<Customer> getCustomer(@PathVariable String name) {
        Customer customer = customerRepository.findByName(name);
        if (customer == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(customer);
    }

    @GetMapping("/{name}/payments")
    public List<Payment> getCustomerPayments(@PathVariable String name) {
        return paymentRepository.findByCustomerName(name);
    }

    @PutMapping("/{name}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable String name,
                                                   @RequestBody Customer customerDetails) {
        Customer customer = customerRepository.findByName(name);
        if (customer == null) {
            return ResponseEntity.notFound().build();
        }

        // Update customer details
        customer.setBalance(customerDetails.getBalance());
        // Any other fields you want to update

        return ResponseEntity.ok(customerRepository.save(customer));
    }
}