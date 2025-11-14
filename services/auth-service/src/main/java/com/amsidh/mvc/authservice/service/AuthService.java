package com.amsidh.mvc.authservice.service;

import com.amsidh.mvc.authservice.dto.AuthRequest;
import com.amsidh.mvc.authservice.dto.AuthResponse;
import com.amsidh.mvc.authservice.dto.UserCredentialRequest;

/**
 * Service interface for authentication operations.
 * 
 * Provides methods for:
 * - User registration with password encryption
 * - JWT token generation upon successful authentication
 * - Token validation for protected endpoints
 * 
 * Security:
 * - Passwords are encrypted using BCrypt before storage
 * - JWT tokens are signed using HS256 algorithm
 * - Tokens contain user email as subject claim
 * 
 * @author Amsidh Mohammed
 */
public interface AuthService {

    /**
     * Registers a new user in the system.
     * 
     * Steps:
     * 1. Validates that email is not already registered
     * 2. Encrypts password using BCrypt
     * 3. Saves user credentials to database
     * 4. Returns success message
     * 
     * @param request the user registration request containing name, email, and
     *                password
     * @return success message confirming user registration
     * @throws com.amsidh.mvc.authservice.exception.UserAlreadyExistsException if
     *                                                                         email
     *                                                                         already
     *                                                                         exists
     */
    String registerUser(UserCredentialRequest request);

    /**
     * Generates a JWT token for authenticated user.
     * 
     * Steps:
     * 1. Validates user credentials (email and password)
     * 2. Verifies password using BCrypt password encoder
     * 3. Generates JWT token with email as subject
     * 4. Returns token with expiration details
     * 
     * @param request the authentication request containing email and password
     * @return AuthResponse containing JWT token, token type, and expiration time
     * @throws com.amsidh.mvc.authservice.exception.AuthenticationException if
     *                                                                      credentials
     *                                                                      are
     *                                                                      invalid
     */
    AuthResponse generateToken(AuthRequest request);

    /**
     * Validates a JWT token and returns the associated user email.
     * 
     * Steps:
     * 1. Extracts email from token claims
     * 2. Validates token signature
     * 3. Checks token expiration
     * 4. Verifies email matches token subject
     * 
     * This method is typically called by:
     * - Gateway service to validate incoming requests
     * - Protected endpoints to verify user identity
     * 
     * @param token the JWT token to validate (without "Bearer " prefix)
     * @return the email address extracted from valid token
     * @throws com.amsidh.mvc.authservice.exception.AuthenticationException if
     *                                                                      token
     *                                                                      is
     *                                                                      invalid
     *                                                                      or
     *                                                                      expired
     */
    String validateToken(String token);
}
