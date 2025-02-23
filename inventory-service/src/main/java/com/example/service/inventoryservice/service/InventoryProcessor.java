package com.example.service.inventoryservice.service;

import com.example.common.config.RabbitMQConfig;
import com.example.common.model.Order;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryProcessor {

    private final ObjectMapper objectMapper;

    @Autowired
    public InventoryProcessor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = RabbitMQConfig.INVENTORY_QUEUE)
    public void processInventory(Message message) {
        try {
            // Debug information
            System.out.println("Received message in INVENTORY service");

            // Convert message body to string for debugging
            String jsonBody = new String(message.getBody());
            System.out.println("Received JSON: " + jsonBody);

            // Parse the JSON to Order object
            Order order = objectMapper.readValue(jsonBody, Order.class);

            // Process the order
            System.out.println("Processing inventory for order: " + order.getOrderId());
            System.out.println("Customer: " + order.getCustomerName());
            System.out.println("Amount: " + order.getTotalPrice());

            // Simulate inventory processing
            Thread.sleep(1000);
            System.out.println("Inventory processed successfully for order: " + order.getOrderId());

        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
            e.printStackTrace();
        }
    }
}