package com.example.paymentservice.repository;

import com.example.paymentservice.model.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface PaymentRepository extends MongoRepository<Payment, String> {
    List<Payment> findByOrderId(String orderId);
    List<Payment> findByCustomerName(String customerName);
}