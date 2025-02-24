package com.example.service.inventoryservice.service;

import com.example.common.config.RabbitMQConfig;
import com.example.common.model.Order;
import com.example.service.inventoryservice.model.InventoryTransaction;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class InventoryProcessor {

    private final ObjectMapper objectMapper;

    @Autowired
    public InventoryProcessor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    // Changed to listen to payment success queue instead of inventory queue
    @RabbitListener(queues = RabbitMQConfig.PAYMENT_SUCCESS_QUEUE)
    public void processInventory(Message message) {
        try {
            System.out.println("Received message in INVENTORY service from payment success queue");

            // Convert message body to string for debugging
            String jsonBody = new String(message.getBody());
            System.out.println("Received JSON: " + jsonBody);

            // Parse the JSON to Order object
            Order order = objectMapper.readValue(jsonBody, Order.class);

            // Process the order
            System.out.println("Processing inventory for order: " + order.getOrderId());
            System.out.println("Customer: " + order.getCustomerName());
            System.out.println("Amount: " + order.getTotalPrice());

            // Create inventory transaction (in a real implementation, we would process actual inventory items)
            createInventoryTransaction(order);

            // Simulate inventory processing
            Thread.sleep(1000);
            System.out.println("Inventory processed successfully for order: " + order.getOrderId() + " after successful payment");

        } catch (Exception e) {
            System.err.println("Error processing inventory message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void createInventoryTransaction(Order order) {
        // This is a simplified example - in a real application, you would:
        // 1. Get the order items from the order
        // 2. Check if there's enough inventory for each item
        // 3. Reserve the inventory

        // For this example, we'll just create a placeholder inventory transaction
        InventoryTransaction transaction = new InventoryTransaction(
                UUID.randomUUID().toString(),
                order.getOrderId(),
                "sample-product-id", // In a real app, you would have product IDs in the order
                1, // Quantity
                "RESERVE",
                System.currentTimeMillis()
        );

        // In a real application, you would save this transaction to a repository
        System.out.println("Created inventory transaction: " + transaction.getId() +
                " for order: " + transaction.getOrderId());
    }
}