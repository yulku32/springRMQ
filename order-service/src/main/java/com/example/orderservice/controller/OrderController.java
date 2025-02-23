package com.example.orderservice.controller;

import com.example.common.model.Order;
import com.example.orderservice.service.OrderPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderPublisher orderPublisher;

    @Autowired
    public OrderController(OrderPublisher orderPublisher) {
        this.orderPublisher = orderPublisher;
    }

    @PostMapping
    public String createOrder(@RequestBody Order order){
        orderPublisher.sendOrder(order);
        return "Order sent: " + order.getOrderId();
    }
}
