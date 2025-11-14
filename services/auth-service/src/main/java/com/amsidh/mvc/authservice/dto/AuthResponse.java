package com.amsidh.mvc.authservice.dto;

import lombok.Builder;

/**
 * Response DTO for successful authentication.
 * 
 * Contains the JWT token and metadata for client use.
 * Client should include this token in the Authorization header for subsequent
 * requests.
 * 
 * Token Usage:
 * Authorization: Bearer <token>
 * 
 * Example:
 * {
 * "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
 * "tokenType": "Bearer",
 * "expiresIn": 86400000
 * }
 * 
 * @param token     the JWT access token
 * @param tokenType the token type (always "Bearer" for JWT)
 * @param expiresIn token expiration time in milliseconds
 * 
 * @author Amsidh Mohammed
 */
@Builder
public record AuthResponse(
        String token,
        String tokenType,
        Long expiresIn) {
    /**
     * Constructor with default token type.
     * Sets tokenType to "Bearer" if not specified.
     */
    public AuthResponse {
        if (tokenType == null || tokenType.isBlank()) {
            tokenType = "Bearer";
        }
    }
}
