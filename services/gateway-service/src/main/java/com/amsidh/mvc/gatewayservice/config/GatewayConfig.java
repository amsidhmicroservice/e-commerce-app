package com.amsidh.mvc.gatewayservice.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuration class for Gateway Service.
 * 
 * Provides beans for:
 * - WebClient.Builder with load balancing for inter-service communication
 * 
 * Load Balancing:
 * - @LoadBalanced enables client-side load balancing via Eureka
 * - Allows using service names instead of host:port in URLs
 * - Example: http://auth-service/... instead of http://localhost:8099/...
 * 
 * @author Amsidh Mohammed
 */
@Configuration
public class GatewayConfig {

    /**
     * Creates a WebClient.Builder bean with load balancing support.
     * 
     * This WebClient.Builder is used by filters to make HTTP requests
     * to other microservices (e.g., auth-service for token validation).
     * 
     * @LoadBalanced annotation enables:
     *               - Service discovery via Eureka
     *               - Client-side load balancing
     *               - Automatic service name resolution
     * 
     *               Usage Example:
     *               webClientBuilder.build()
     *               .get()
     *               .uri("http://auth-service/api/v1/auth-service/auth/validate")
     *               .retrieve()
     *               .bodyToMono(String.class);
     * 
     *               Benefits:
     *               - No hardcoded host:port (uses service name from Eureka)
     *               - Automatic failover if service instance is down
     *               - Distributes load across multiple service instances
     * 
     * @return WebClient.Builder with load balancing
     */
    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
