package com.example.common.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

//@Component
//public class RabbitMQInitializer implements ApplicationListener<ApplicationReadyEvent> {
//
//    private final AmqpAdmin amqpAdmin;
//    private final Queue paymentQueue;
//    private final Queue inventoryQueue;
//    private final FanoutExchange orderExchange;
//    private final Binding paymentBinding;
//    private final Binding inventoryBinding;
//
//    public RabbitMQInitializer(AmqpAdmin amqpAdmin,
//                               Queue paymentQueue,
//                               Queue inventoryQueue,
//                               FanoutExchange orderExchange,
//                               Binding paymentBinding,
//                               Binding inventoryBinding) {
//        this.amqpAdmin = amqpAdmin;
//        this.paymentQueue = paymentQueue;
//        this.inventoryQueue = inventoryQueue;
//        this.orderExchange = orderExchange;
//        this.paymentBinding = paymentBinding;
//        this.inventoryBinding = inventoryBinding;
//    }
//
//    @Override
//    public void onApplicationEvent(ApplicationReadyEvent event) {
//        // Create Exchange
//        amqpAdmin.declareExchange(orderExchange);
//
//        // Create Queues
//        amqpAdmin.declareQueue(paymentQueue);
//        amqpAdmin.declareQueue(inventoryQueue);
//
//        // Create Bindings
//        amqpAdmin.declareBinding(paymentBinding);
//        amqpAdmin.declareBinding(inventoryBinding);
//    }
//}