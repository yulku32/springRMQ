//package com.example.orderservice.controller;
//
//import com.example.common.model.Order;
//import com.example.orderservice.model.OrderEntity;
//import com.example.orderservice.service.OrderService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/orders")
//public class OrderController {
//    private final OrderService orderService;
//
//    @Autowired
//    public OrderController(OrderService orderService) {
//        this.orderService = orderService;
//    }
//
//    @PostMapping
//    public ResponseEntity<OrderEntity> createOrder(@RequestBody Order order) {
//        OrderEntity savedOrder = orderService.createOrder(order);
//        return ResponseEntity.ok(savedOrder);
//    }
//
//    @GetMapping
//    public List<OrderEntity> getAllOrders() {
//        return orderService.getAllOrders();
//    }
//
//    @GetMapping("/{orderId}")
//    public ResponseEntity<OrderEntity> getOrderById(@PathVariable String orderId) {
//        OrderEntity order = orderService.getOrderById(orderId);
//        if (order == null) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(order);
//    }
//
//    @GetMapping("/customer/{customerName}")
//    public List<OrderEntity> getOrdersByCustomer(@PathVariable String customerName) {
//        return orderService.getOrdersByCustomer(customerName);
//    }
//}



package com.example.orderservice.controller;

import com.example.common.model.Order;
import com.example.orderservice.service.OrderPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderPublisher orderPublisher;

    // In-memory storage
    private final Map<String, Order> orders = new HashMap<>();

    @Autowired
    public OrderController(OrderPublisher orderPublisher) {
        this.orderPublisher = orderPublisher;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        // Save to in-memory map
        orders.put(order.getOrderId(), order);

        // Send to message queue
        orderPublisher.sendOrder(order);

        System.out.println("Order processed: " + order.getOrderId());
        return ResponseEntity.ok(order);
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(new ArrayList<>(orders.values()));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable String orderId) {
        Order order = orders.get(orderId);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(order);
    }

    @GetMapping("/customer/{customerName}")
    public ResponseEntity<List<Order>> getOrdersByCustomer(@PathVariable String customerName) {
        List<Order> customerOrders = new ArrayList<>();
        for (Order order : orders.values()) {
            if (order.getCustomerName().equals(customerName)) {
                customerOrders.add(order);
            }
        }
        return ResponseEntity.ok(customerOrders);
    }
}