package com.amsidh.mvc.authservice.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amsidh.mvc.authservice.dto.AuthRequest;
import com.amsidh.mvc.authservice.dto.AuthResponse;
import com.amsidh.mvc.authservice.dto.UserCredentialRequest;
import com.amsidh.mvc.authservice.entity.UserCredential;
import com.amsidh.mvc.authservice.exception.AuthenticationException;
import com.amsidh.mvc.authservice.exception.UserAlreadyExistsException;
import com.amsidh.mvc.authservice.repository.UserCredentialRepository;
import com.amsidh.mvc.authservice.service.AuthService;
import com.amsidh.mvc.authservice.util.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of authentication service.
 * 
 * Handles:
 * - User registration with BCrypt password encryption
 * - Token generation with credential validation
 * - Token validation for API Gateway
 * 
 * Dependencies:
 * - UserCredentialRepository: Database operations
 * - PasswordEncoder: BCrypt password hashing
 * - JwtUtil: JWT token operations
 * 
 * Security Features:
 * - BCrypt password hashing (cost factor 10)
 * - JWT token with HS256 signing
 * - Transaction management for data consistency
 * - Comprehensive audit logging
 * 
 * @author Amsidh Mohammed
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserCredentialRepository userCredentialRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * {@inheritDoc}
     * 
     * Implementation details:
     * - Checks email uniqueness using indexed query
     * - Uses BCrypt for password hashing (cost 10, secure random salt)
     * - Wrapped in transaction for atomicity
     * - Logs registration events for audit trail
     */
    @Override
    @Transactional
    public String registerUser(UserCredentialRequest request) {
        log.info("Attempting to register new user with email: {}", request.email());

        // Check if user already exists
        if (userCredentialRepository.existsByEmail(request.email())) {
            log.warn("Registration failed: Email {} already exists", request.email());
            throw new UserAlreadyExistsException(
                    "User with email " + request.email() + " already exists");
        }

        // Check if username is already taken
        if (userCredentialRepository.findByName(request.name()).isPresent()) {
            log.warn("Registration failed: Username {} already exists", request.name());
            throw new UserAlreadyExistsException(
                    "User with name " + request.name() + " already exists");
        }

        // Create new user with encrypted password
        UserCredential user = UserCredential.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password())) // BCrypt encryption
                .build();

        userCredentialRepository.save(user);
        log.info("User registered successfully with email: {}", request.email());

        return "User registered successfully";
    }

    /**
     * {@inheritDoc}
     * 
     * Implementation details:
     * - Retrieves user by email (indexed query)
     * - Validates password using BCrypt matches() method
     * - Generates JWT with 24-hour expiration
     * - Returns token with metadata
     * 
     * Password validation:
     * BCrypt automatically extracts salt from stored hash
     * and compares with provided password
     */
    @Override
    @Transactional(readOnly = true)
    public AuthResponse generateToken(AuthRequest request) {
        log.info("Generating token for user: {}", request.email());

        // Retrieve user by email
        UserCredential user = userCredentialRepository.findByEmail(request.email())
                .orElseThrow(() -> {
                    log.error("Authentication failed: User not found with email: {}", request.email());
                    return new AuthenticationException("Invalid credentials");
                });

        // Validate password using BCrypt
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            log.error("Authentication failed: Invalid password for email: {}", request.email());
            throw new AuthenticationException("Invalid credentials");
        }

        // Generate JWT token
        String token = jwtUtil.generateToken(user.getEmail());
        log.info("Token generated successfully for user: {}", request.email());

        // Return token response with metadata
        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .expiresIn(86400000L) // 24 hours in milliseconds
                .build();
    }

    /**
     * {@inheritDoc}
     * 
     * Implementation details:
     * - Extracts email from token claims (subject)
     * - Validates signature using secret key
     * - Checks expiration timestamp
     * - Returns email for downstream authorization
     * 
     * Gateway Integration:
     * Gateway calls this endpoint to validate tokens
     * and extract user identity for request headers
     */
    @Override
    @Transactional(readOnly = true)
    public String validateToken(String token) {
        log.debug("Validating token");

        try {
            // Extract email from token
            String email = jwtUtil.extractEmail(token);
            log.debug("Extracted email from token: {}", email);

            // Validate token (signature + expiration + subject)
            jwtUtil.validateToken(token, email);
            log.debug("Token validated successfully for email: {}", email);

            return email;

        } catch (AuthenticationException e) {
            log.error("Token validation failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during token validation", e);
            throw new AuthenticationException("Token validation failed: " + e.getMessage());
        }
    }
}
