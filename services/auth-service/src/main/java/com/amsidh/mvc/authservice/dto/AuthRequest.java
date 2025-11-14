package com.amsidh.mvc.authservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

/**
 * Request DTO for user authentication (login).
 * 
 * Contains user credentials for token generation.
 * Email is used as the primary authentication identifier.
 * 
 * Validation Rules:
 * - Email: Required, non-blank
 * - Password: Required, non-blank
 * 
 * Security Note:
 * This password is in plain text during transmission but should be:
 * - Sent over HTTPS in production
 * - Never logged or stored in plain text
 * - Compared against BCrypt hash in database
 * 
 * @param email    the user's email address
 * @param password the user's password (plain text for authentication)
 * 
 * @author Amsidh Mohammed
 */
@Builder
public record AuthRequest(

        @NotBlank(message = "Email is required") String email,

        @NotBlank(message = "Password is required") String password) {
}
