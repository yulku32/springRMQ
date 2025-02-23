package com.example.common.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Order implements Serializable {
    private String orderId;
    private String customerName;
    private double totalPrice;

    public Order() {}

    @JsonCreator
    public Order(@JsonProperty("orderId") String orderId,
                 @JsonProperty("customerName") String customerName,
                 @JsonProperty("totalPrice") double totalPrice) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.totalPrice = totalPrice;
    }

    public String getOrderId() { return orderId; }
    public String getCustomerName() { return customerName; }
    public double getTotalPrice() { return totalPrice; }
}
