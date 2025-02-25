package com.example.orderservice.service;

import com.example.common.model.Order;
import com.example.orderservice.model.OrderEntity;
import com.example.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderPublisher orderPublisher;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderPublisher orderPublisher) {
        this.orderRepository = orderRepository;
        this.orderPublisher = orderPublisher;
    }

    public OrderEntity createOrder(Order order) {
        // First save the order to the database
        OrderEntity orderEntity = OrderEntity.fromOrder(order);
        OrderEntity savedOrder = orderRepository.save(orderEntity);

        // Then send it to the message queue for further processing
        orderPublisher.sendOrder(order);

        return savedOrder;
    }

    public List<OrderEntity> getAllOrders() {
        return orderRepository.findAll();
    }

    public OrderEntity getOrderById(String orderId) {
        return orderRepository.findByOrderId(orderId);
    }

    public List<OrderEntity> getOrdersByCustomer(String customerName) {
        return orderRepository.findByCustomerName(customerName);
    }
}