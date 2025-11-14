package com.amsidh.mvc.authservice.exception;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Standard error response format for all API errors.
 * 
 * Provides consistent error structure across all endpoints:
 * - timestamp: When the error occurred
 * - status: HTTP status code
 * - error: HTTP status reason phrase
 * - message: Human-readable error message
 * - path: Request URI where error occurred
 * - validationErrors: List of field validation errors (optional)
 * 
 * Example Response (Authentication Error):
 * {
 * "timestamp": "2025-10-31T10:15:30",
 * "status": 401,
 * "error": "Unauthorized",
 * "message": "Invalid credentials",
 * "path": "/api/v1/auth-service/auth/token"
 * }
 * 
 * Example Response (Validation Error):
 * {
 * "timestamp": "2025-10-31T10:15:30",
 * "status": 400,
 * "error": "Bad Request",
 * "message": "Validation failed",
 * "path": "/api/v1/auth-service/auth/register",
 * "validationErrors": [
 * "email: must be a well-formed email address",
 * "password: size must be between 6 and 2147483647"
 * ]
 * }
 * 
 * @author Amsidh Mohammed
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    /**
     * Timestamp when the error occurred.
     * Format: ISO 8601 (yyyy-MM-dd'T'HH:mm:ss)
     */
    private LocalDateTime timestamp;

    /**
     * HTTP status code (e.g., 400, 401, 404, 500).
     */
    private int status;

    /**
     * HTTP status reason phrase (e.g., "Bad Request", "Unauthorized").
     */
    private String error;

    /**
     * Human-readable error message describing what went wrong.
     * Should be safe to display to end users.
     */
    private String message;

    /**
     * The request URI where the error occurred.
     * Helps identify which endpoint caused the error.
     */
    private String path;

    /**
     * List of field validation errors (optional).
     * Only present for validation failures (400 Bad Request).
     * Each entry describes a specific field validation failure.
     */
    private List<String> validationErrors;
}
