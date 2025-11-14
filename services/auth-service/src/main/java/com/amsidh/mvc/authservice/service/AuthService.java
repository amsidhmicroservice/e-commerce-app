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

    String registerUser(UserCredentialRequest request);

    AuthResponse generateToken(AuthRequest request);

}
