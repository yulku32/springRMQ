package com.example.orderservice.model;

import com.example.common.model.Order;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "orders")
public class OrderEntity {
    @Id
    private String id;
    private String orderId;
    private String customerName;
    private double totalPrice;
    private String status;
    private LocalDateTime createdAt;

    public OrderEntity() {
    }

    // Create from Order DTO
    public static OrderEntity fromOrder(Order order) {
        OrderEntity entity = new OrderEntity();
        entity.setOrderId(order.getOrderId());
        entity.setCustomerName(order.getCustomerName());
        entity.setTotalPrice(order.getTotalPrice());
        entity.setStatus("CREATED");
        entity.setCreatedAt(LocalDateTime.now());
        return entity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}