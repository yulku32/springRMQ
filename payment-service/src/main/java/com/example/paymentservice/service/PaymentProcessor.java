package com.example.paymentservice.service;

import com.example.common.config.RabbitMQConfig;
import com.example.common.model.Order;
import com.example.paymentservice.model.Customer;
import com.example.paymentservice.model.Payment;
import com.example.paymentservice.repository.CustomerRepository;
import com.example.paymentservice.repository.PaymentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentProcessor {
    private final CustomerRepository customerRepository;
    private final PaymentRepository paymentRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public PaymentProcessor(CustomerRepository customerRepository,
                            PaymentRepository paymentRepository,
                            ObjectMapper objectMapper) {
        this.customerRepository = customerRepository;
        this.paymentRepository = paymentRepository;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = RabbitMQConfig.PAYMENT_QUEUE)
    public void processPayment(Message message) {
        try {
            // Parse message
            String jsonBody = new String(message.getBody());
            Order order = objectMapper.readValue(jsonBody, Order.class);

            // Check if customer exists and has enough balance
            Customer customer = customerRepository.findByName(order.getCustomerName());
            if (customer == null) {
                System.out.println("Customer not found: " + order.getCustomerName());
                // Create payment record with FAILED status
                savePayment(order, "CUSTOMER_NOT_FOUND");
                return;
            }

            if (customer.getBalance() < order.getTotalPrice()) {
                System.out.println("Insufficient funds for: " + order.getOrderId());
                // Create payment record with FAILED status
                savePayment(order, "INSUFFICIENT_FUNDS");
                return;
            }

            // Process payment
            customer.setBalance(customer.getBalance() - order.getTotalPrice());
            customerRepository.save(customer);

            // Create payment record with SUCCESS status
            savePayment(order, "SUCCESS");

            System.out.println("Payment processed successfully for order: " + order.getOrderId());
        } catch (Exception e) {
            System.err.println("Error processing payment: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void savePayment(Order order, String status) {
        Payment payment = new Payment();
        payment.setOrderId(order.getOrderId());
        payment.setCustomerName(order.getCustomerName());
        payment.setAmount(order.getTotalPrice());
        payment.setStatus(status);
        payment.setTimestamp(System.currentTimeMillis());
        paymentRepository.save(payment);
    }
}