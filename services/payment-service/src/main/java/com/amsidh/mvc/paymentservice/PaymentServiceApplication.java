package com.amsidh.mvc.paymentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.TimeZone;

/**
 * Main class for the Payment Service.
 * This service handles payment processing for orders and sends payment
 * notifications via Kafka.
 * Uses JPA for database operations with automatic timestamp auditing.
 */
@SpringBootApplication
@EnableJpaAuditing
public class PaymentServiceApplication {

	/**
	 * Entry point for the Payment Service application.
	 * Sets default timezone to Asia/Kolkata.
	 * 
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
		SpringApplication.run(PaymentServiceApplication.class, args);
	}

}
