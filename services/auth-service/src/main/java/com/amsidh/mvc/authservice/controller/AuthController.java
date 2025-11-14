package com.amsidh.mvc.authservice.controller;

import com.amsidh.mvc.authservice.dto.AuthRequest;
import com.amsidh.mvc.authservice.dto.AuthResponse;
import com.amsidh.mvc.authservice.dto.UserCredentialRequest;
import com.amsidh.mvc.authservice.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for authentication endpoints.
 * 
 * Base URL: /api/v1/auth-service/auth
 * 
 * Endpoints:
 * - POST /register - User registration
 * - POST /token - Login and JWT token generation
 * - GET /validate - JWT token validation
 * 
 * All endpoints are public (no authentication required).
 * Gateway is responsible for validating tokens for business services.
 * 
 * Architecture Flow:
 * 1. Client registers → POST /register
 * 2. Client logs in → POST /token → Receives JWT
 * 3. Client accesses business service → Gateway intercepts
 * 4. Gateway calls → GET /validate?token=<jwt>
 * 5. Auth-service validates token → Returns email
 * 6. Gateway routes request with user context
 * 
 * @author Amsidh Mohammed
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "Authentication and authorization endpoints for user management")
public class AuthController {

    private final AuthService authService;

    /**
     * Register a new user.
     * 
     * Creates a new user account with encrypted password.
     * Email must be unique across all users.
     * 
     * Request Body:
     * {
     * "name": "John Doe",
     * "email": "john.doe@example.com",
     * "password": "password123"
     * }
     * 
     * Success Response: 201 CREATED
     * {
     * "message": "User registered successfully"
     * }
     * 
     * Error Responses:
     * - 400 BAD REQUEST: Validation errors (invalid email, short password)
     * - 409 CONFLICT: Email or username already exists
     * - 500 INTERNAL SERVER ERROR: Server error
     * 
     * @param request the user registration request
     * @return success message with 201 status
     */
    @PostMapping("/register")
    @Operation(summary = "Register new user", description = "Creates a new user account with encrypted password. Email must be unique.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "User already exists", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<String> register(
            @Valid @RequestBody @Parameter(description = "User registration details", required = true) UserCredentialRequest request) {

        log.info("Registration request received for email: {}", request.email());
        String message = authService.registerUser(request);
        log.info("User registered successfully: {}", request.email());

        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    /**
     * Generate JWT token (Login).
     * 
     * Authenticates user credentials and generates JWT token.
     * Token is valid for 24 hours.
     * 
     * Request Body:
     * {
     * "email": "john.doe@example.com",
     * "password": "password123"
     * }
     * 
     * Success Response: 200 OK
     * {
     * "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
     * "tokenType": "Bearer",
     * "expiresIn": 86400000
     * }
     * 
     * Error Responses:
     * - 400 BAD REQUEST: Missing email or password
     * - 401 UNAUTHORIZED: Invalid credentials
     * - 500 INTERNAL SERVER ERROR: Server error
     * 
     * Usage:
     * Client should store the token and include it in subsequent requests:
     * Authorization: Bearer <token>
     * 
     * @param request the authentication request with email and password
     * @return JWT token with metadata
     */
    @PostMapping("/token")
    @Operation(summary = "Generate JWT token", description = "Authenticates user and generates JWT token valid for 24 hours")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token generated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<AuthResponse> generateToken(
            @Valid @RequestBody @Parameter(description = "User credentials for authentication", required = true) AuthRequest request) {

        log.info("Token generation request received for email: {}", request.email());
        AuthResponse response = authService.generateToken(request);
        log.info("Token generated successfully for email: {}", request.email());

        return ResponseEntity.ok(response);
    }

}
