package com.example.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@EnableRabbit
public class RabbitMQConfig {
    public static final String EXCHANGE_NAME = "orders.exchange";
    public static final String PAYMENT_QUEUE = "orders.payment.queue";
    public static final String INVENTORY_QUEUE = "orders.inventory.queue";
    public static final String PAYMENT_STATUS_EXCHANGE = "payment.status.exchange";

    @Value("${spring.rabbitmq.host:localhost}")
    private String host;

    @Value("${spring.rabbitmq.port:5672}")
    private int port;

    @Value("${spring.rabbitmq.username:guest}")
    private String username;

    @Value("${spring.rabbitmq.password:guest}")
    private String password;

    @Bean("paymentQueue")
    public Queue paymentQueue() {
        return new Queue(PAYMENT_QUEUE, true);
    }

    @Bean("inventoryQueue")
    public Queue inventoryQueue() {
        return new Queue(INVENTORY_QUEUE, true);
    }

    @Bean
    public FanoutExchange ordersExchange() {
        return new FanoutExchange(EXCHANGE_NAME);
    }

    @Bean
    public TopicExchange paymentStatusExchange() {
        return new TopicExchange(PAYMENT_STATUS_EXCHANGE);
    }

    @Bean
    public Binding paymentBinding(@Qualifier("paymentQueue") Queue paymentQueue, FanoutExchange ordersExchange) {
        return BindingBuilder.bind(paymentQueue).to(ordersExchange);
    }

    @Bean
    public Binding inventoryBinding(@Qualifier("inventoryQueue") Queue inventoryQueue, FanoutExchange ordersExchange) {
        return BindingBuilder.bind(inventoryQueue).to(ordersExchange);
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host, port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        return connectionFactory;
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    @Primary
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        template.setExchange(EXCHANGE_NAME);
        return template;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}