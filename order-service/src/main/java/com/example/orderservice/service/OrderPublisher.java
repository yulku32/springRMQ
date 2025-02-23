package com.example.orderservice.service;

import com.example.common.config.RabbitMQConfig;
import com.example.common.model.Order;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderPublisher {
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public OrderPublisher(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendOrder(Order order) {
        try {
            // Manually convert to JSON
            String jsonOrder = objectMapper.writeValueAsString(order);
            System.out.println("Sending JSON payload: " + jsonOrder);

            // Create message with JSON content type
            Message message = MessageBuilder
                    .withBody(jsonOrder.getBytes())
                    .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                    .setHeader("__TypeId__", Order.class.getName())
                    .build();

            // Send the message - with fanout exchange, routing key is ignored
            rabbitTemplate.send(RabbitMQConfig.EXCHANGE_NAME, "", message);

            System.out.println("Order Sent: " + order.getOrderId());

        } catch (Exception e) {
            System.err.println("Error sending message: " + e.getMessage());
            e.printStackTrace();
        }
    }
}