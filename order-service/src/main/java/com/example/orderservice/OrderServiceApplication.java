package com.example.orderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// Remove the import if it's there
// import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
// Remove this annotation to avoid duplication
// @EnableMongoRepositories(basePackages = "com.example.orderservice.repository")
public class OrderServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApplication.class, args);
	}
}