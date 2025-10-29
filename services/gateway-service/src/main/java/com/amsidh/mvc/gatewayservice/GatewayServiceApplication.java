package com.amsidh.mvc.gatewayservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class for the Gateway Service.
 * This service acts as an API Gateway, routing requests to appropriate
 * microservices.
 * It provides centralized routing, load balancing, and cross-cutting concerns
 * like logging and security.
 */
@SpringBootApplication
public class GatewayServiceApplication {
	/**
	 * Entry point for the Gateway Service application.
	 * 
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(GatewayServiceApplication.class, args);
	}
}
