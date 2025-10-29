package com.amsidh.mvc.orderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.TimeZone;

/**
 * Main class for the Order Service.
 * This service manages customer orders, integrating with customer, product,
 * payment, and notification services.
 * It uses Feign clients for inter-service communication and JPA for database
 * operations.
 */
@SpringBootApplication
@EnableFeignClients
@EnableJpaAuditing
public class OrderServiceApplication {

    /**
     * Entry point for the Order Service application.
     * Sets default timezone to Asia/Kolkata.
     * 
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
        SpringApplication.run(OrderServiceApplication.class, args);
    }

}
