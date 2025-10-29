package com.amsidh.mvc.productservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

/**
 * Main class for the Product Service.
 * This service manages product catalog including CRUD operations, product
 * purchases, and inventory management.
 * Products are organized into categories and uses JPA for database persistence.
 */
@SpringBootApplication
public class ProductServiceApplication {

    /**
     * Entry point for the Product Service application.
     * Sets default timezone to Asia/Kolkata.
     * 
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
        SpringApplication.run(ProductServiceApplication.class, args);
    }

}
