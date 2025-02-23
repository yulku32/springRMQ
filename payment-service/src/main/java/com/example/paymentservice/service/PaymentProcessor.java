package com.example.paymentservice.service;

import com.example.common.config.RabbitMQConfig;
import com.example.common.model.Order;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentProcessor {

    private final ObjectMapper objectMapper;

    @Autowired
    public PaymentProcessor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void processPayment(Message message) {
        try {
            // Convert message body to string for debugging
            String jsonBody = new String(message.getBody());
            System.out.println("Received JSON: " + jsonBody);

            // Parse the JSON to Order object
            Order order = objectMapper.readValue(jsonBody, Order.class);

            // Process the order
            System.out.println("Processing payment for order: " + order.getOrderId());
            System.out.println("Customer: " + order.getCustomerName());
            System.out.println("Amount: " + order.getTotalPrice());

            // Simulate payment processing
            Thread.sleep(1000);
            System.out.println("Payment processed successfully for order: " + order.getOrderId());

        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
            e.printStackTrace();
        }
    }
}