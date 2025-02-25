package com.example.orderservice.repository;

import com.example.orderservice.model.OrderEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface OrderRepository extends MongoRepository<OrderEntity, String> {
    OrderEntity findByOrderId(String orderId);
    List<OrderEntity> findByCustomerName(String customerName);
}