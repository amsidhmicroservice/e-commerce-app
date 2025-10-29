package com.amsidh.mvc.customerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Main class for the Customer Service microservice.
 * This service manages customer data including creation, updates, and retrieval
 * operations.
 * It integrates with Eureka for service discovery and uses MongoDB for data
 * persistence.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class CustomerServiceApplication {

    /**
     * Entry point for the Customer Service application.
     * 
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }

}
