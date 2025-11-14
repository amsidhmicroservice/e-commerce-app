package com.amsidh.mvc.authservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for authentication service.
 * 
 * Provides:
 * - Password encoder bean (BCrypt)
 * 
 * BCrypt Configuration:
 * - Algorithm: BCrypt with secure random salt
 * - Cost factor: 10 (2^10 = 1024 iterations)
 * - Salt: Randomly generated per password
 * - Output: 60-character hash string
 * 
 * Security Notes:
 * - BCrypt is designed to be slow to resist brute-force attacks
 * - Cost factor can be increased for stronger security (at performance cost)
 * - Each password gets unique salt, preventing rainbow table attacks
 * 
 * No SecurityFilterChain needed because:
 * - Auth-service only provides authentication endpoints
 * - All endpoints are public (/register, /token, /validate)
 * - Actual authorization is handled by API Gateway
 * - Gateway validates tokens before routing to business services
 * 
 * Architecture:
 * Client → Gateway (validates JWT) → Business Services
 * ↓ (calls /validate)
 * Auth Service
 * 
 * @author Amsidh Mohammed
 */
@Configuration
@EnableWebSecurity
public class AuthConfig {

    /**
     * Configures Spring Security for Auth Service.
     * 
     * Security Configuration:
     * - All endpoints are public (no authentication required at service level)
     * - CSRF disabled (stateless REST API with JWT)
     * - Session management: STATELESS (no server-side sessions)
     * - CORS handled by API Gateway
     * 
     * Public Endpoints (All endpoints are public):
     * - POST /api/v1/auth-service/auth/register - User registration
     * - POST /api/v1/auth-service/auth/token - User login and JWT generation
     * - GET /api/v1/auth-service/auth/validate - JWT token validation (called by
     * Gateway)
     * - /actuator/** - Health checks and metrics
     * 
     * Architecture Rationale:
     * - Auth Service should NOT authenticate its own endpoints
     * - API Gateway is responsible for enforcing authentication on business
     * services
     * - /register and /token MUST be public (bootstrap authentication)
     * - /validate is public but only called internally by Gateway
     * - Gateway's AuthenticationFilter protects all other services
     * 
     * Security Flow:
     * 1. User registers/logs in → Auth Service (NO authentication)
     * 2. User accesses business service → Gateway validates JWT → Calls /validate
     * 3. Gateway adds user context → Routes to business service
     * 
     * @param http HttpSecurity configuration
     * @return SecurityFilterChain configured for stateless REST API
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for stateless REST API
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/v1/auth-service/auth/register",
                                "/api/v1/auth-service/auth/token",
                                "/api/v1/auth-service/auth/validate",
                                "/actuator/**")
                        .permitAll() // Explicitly permit public endpoints
                        .anyRequest().permitAll() // Permit any other requests (fallback)
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // No sessions
                );

        return http.build();
    }

    /**
     * Creates BCrypt password encoder bean.
     * 
     * BCrypt Details:
     * - Uses Blowfish cipher
     * - Generates random salt per password
     * - Cost factor: 10 (default, balanced between security and performance)
     * - Encoded format: $2a$10$[22-char salt][31-char hash]
     * 
     * Usage:
     * - encode(rawPassword): Hash password for storage
     * - matches(rawPassword, encodedPassword): Verify password during login
     * 
     * Performance:
     * - Encoding: ~100-200ms (intentionally slow)
     * - Matching: Same as encoding (prevents timing attacks)
     * 
     * @return BCrypt password encoder with default cost factor (10)
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
