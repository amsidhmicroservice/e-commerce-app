package com.amsidh.mvc.authservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Integration test to verify that the Spring Boot application context loads
 * successfully.
 * Uses H2 in-memory database for testing (configured in
 * test/resources/application.properties).
 * Disables Eureka and Zipkin for isolated testing.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthServiceApplicationTests {

    @Test
    void contextLoads() {
        // Test that the application context loads successfully
        // This verifies that all beans are properly configured and wired
    }

}
