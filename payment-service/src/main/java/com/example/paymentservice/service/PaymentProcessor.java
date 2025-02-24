package com.example.paymentservice.service;

import com.example.common.config.RabbitMQConfig;
import com.example.common.model.Order;
import com.example.paymentservice.model.Customer;
import com.example.paymentservice.model.Payment;
import com.example.paymentservice.repository.CustomerRepository;
import com.example.paymentservice.repository.PaymentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class PaymentProcessor {
    private final CustomerRepository customerRepository;
    private final PaymentRepository paymentRepository;
    private final ObjectMapper objectMapper;
    private final RabbitTemplate paymentStatusRabbitTemplate;

    @Autowired
    public PaymentProcessor(CustomerRepository customerRepository,
                            PaymentRepository paymentRepository,
                            ObjectMapper objectMapper,
                            @Qualifier("paymentStatusRabbitTemplate") RabbitTemplate paymentStatusRabbitTemplate) {
        this.customerRepository = customerRepository;
        this.paymentRepository = paymentRepository;
        this.objectMapper = objectMapper;
        this.paymentStatusRabbitTemplate = paymentStatusRabbitTemplate;
    }

    @RabbitListener(queues = RabbitMQConfig.PAYMENT_QUEUE)
    public void processPayment(Message message) {
        try {
            // Parse message
            String jsonBody = new String(message.getBody());
            System.out.println("Payment service received order: " + jsonBody);

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

            // Forward the order to inventory service after successful payment
            forwardToInventory(order);

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

    private void forwardToInventory(Order order) {
        try {
            // Convert order to JSON
            String jsonOrder = objectMapper.writeValueAsString(order);

            // Create message with JSON content type
            Message message = MessageBuilder
                    .withBody(jsonOrder.getBytes())
                    .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                    .setHeader("__TypeId__", Order.class.getName())
                    .build();

            // Send to the payment success queue via payment status exchange
            paymentStatusRabbitTemplate.send(
                    RabbitMQConfig.PAYMENT_SUCCESS_ROUTING_KEY,
                    message
            );

            System.out.println("Order forwarded to inventory after successful payment: " + order.getOrderId());
        } catch (Exception e) {
            System.err.println("Error forwarding order to inventory: " + e.getMessage());
            e.printStackTrace();
        }
    }
}