package com.amsidh.mvc.authservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

/**
 * Request DTO for user registration.
 * 
 * Contains validation rules for user input during registration.
 * All fields are validated before processing to ensure data quality.
 * 
 * Validation Rules:
 * - Name: Required, non-blank
 * - Email: Required, valid email format
 * - Password: Required, minimum 6 characters
 * 
 * @param name     the unique username for the account
 * @param email    the unique email address for authentication
 * @param password the plain text password (will be encrypted before storage)
 * 
 * @author Amsidh Mohammed
 */
@Builder
public record UserCredentialRequest(

        @NotBlank(message = "Name is required") String name,

        @Email(message = "Invalid email format") @NotBlank(message = "Email is required") String email,

        @NotBlank(message = "Password is required") @Size(min = 6, message = "Password must be at least 6 characters long") String password) {
}
