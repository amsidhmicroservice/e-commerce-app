package com.amsidh.mvc.notificationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Main class for the Notification Service.
 * This service handles sending notifications (email) for order confirmations
 * and payment notifications.
 * It consumes messages from Kafka topics and processes them asynchronously.
 */
@SpringBootApplication
@EnableAsync
public class NotificationServiceApplication {

    /**
     * Entry point for the Notification Service application.
     * 
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

}
