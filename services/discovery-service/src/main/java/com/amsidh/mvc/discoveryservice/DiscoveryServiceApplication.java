package com.amsidh.mvc.discoveryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Main class for the Discovery Service (Eureka Server).
 * This service provides service registration and discovery capabilities for all
 * microservices.
 * Acts as a service registry where microservices register themselves and
 * discover other services.
 */
@SpringBootApplication
@EnableEurekaServer
public class DiscoveryServiceApplication {

	/**
	 * Entry point for the Discovery Service application.
	 * 
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(DiscoveryServiceApplication.class, args);
	}

}
