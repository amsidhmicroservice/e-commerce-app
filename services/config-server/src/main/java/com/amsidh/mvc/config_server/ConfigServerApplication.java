package com.amsidh.mvc.config_server;

/**
 * Main class for the Config Server microservice.
 * This service provides centralized configuration management for all microservices in the system.
 * It uses Spring Cloud Config Server to serve configuration properties from a remote location.
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {

	/**
	 * Entry point for the Config Server application.
	 * 
	 * @param args command-line arguments
	 */

	public static void main(String[] args) {
		SpringApplication.run(ConfigServerApplication.class, args);
	}

}
