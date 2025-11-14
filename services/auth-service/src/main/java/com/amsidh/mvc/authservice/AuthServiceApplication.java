package com.amsidh.mvc.authservice;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Main application class for Auth Service.
 * 
 * This service handles JWT-based authentication and authorization for the
 * e-commerce microservices architecture.
 * 
 * Architecture Note:
 * - This service is registered with Eureka Discovery Service
 * - All requests MUST come through API Gateway (port 8080)
 * - Direct access to this service (port 8099) should be blocked in production
 * - Provides endpoints for user registration, token generation, and token
 * validation
 * 
 * Security Flow:
 * 1. Client registers/authenticates via Gateway → Auth Service
 * 2. Auth Service returns JWT token
 * 3. Client includes token in subsequent requests to Gateway
 * 4. Gateway validates token with Auth Service before routing to business
 * services
 * 
 * @author Amsidh Mohammed
 * @version 1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
public class AuthServiceApplication {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
        SpringApplication.run(AuthServiceApplication.class, args);
    }

}
