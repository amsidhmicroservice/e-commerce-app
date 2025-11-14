package com.amsidh.mvc.authservice.exception;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * Global exception handler for the authentication service.
 * 
 * Handles all exceptions thrown by controllers and services,
 * converting them to consistent JSON error responses.
 * 
 * Exception Mapping:
 * - UserAlreadyExistsException → 409 CONFLICT
 * - AuthenticationException → 401 UNAUTHORIZED
 * - MethodArgumentNotValidException → 400 BAD REQUEST (with validation details)
 * - Generic Exception → 500 INTERNAL SERVER ERROR
 * 
 * Benefits:
 * - Consistent error response format across all endpoints
 * - Prevents stack traces from leaking to clients
 * - Provides meaningful error messages
 * - Logs errors for debugging and monitoring
 * 
 * @author Amsidh Mohammed
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handles UserAlreadyExistsException (409 CONFLICT).
     * 
     * Thrown when:
     * - User tries to register with existing email
     * - User tries to register with existing username
     * 
     * Response: 409 CONFLICT
     * {
     * "timestamp": "2025-10-31T10:15:30",
     * "status": 409,
     * "error": "Conflict",
     * "message": "User with email john@example.com already exists",
     * "path": "/api/v1/auth-service/auth/register"
     * }
     * 
     * @param ex      the UserAlreadyExistsException
     * @param request the HTTP request
     * @return error response with 409 status
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(
            UserAlreadyExistsException ex,
            HttpServletRequest request) {

        log.warn("User already exists: {} - Path: {}", ex.getMessage(), request.getRequestURI());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error(HttpStatus.CONFLICT.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    /**
     * Handles AuthenticationException (401 UNAUTHORIZED).
     * 
     * Thrown when:
     * - Invalid login credentials (wrong email or password)
     * - Invalid JWT token
     * - Expired JWT token
     * - Token validation failures
     * 
     * Response: 401 UNAUTHORIZED
     * {
     * "timestamp": "2025-10-31T10:15:30",
     * "status": 401,
     * "error": "Unauthorized",
     * "message": "Invalid credentials",
     * "path": "/api/v1/auth-service/auth/token"
     * }
     * 
     * Security Note:
     * Error messages are intentionally generic to prevent
     * email enumeration attacks (don't reveal if user exists).
     * 
     * @param ex      the AuthenticationException
     * @param request the HTTP request
     * @return error response with 401 status
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(
            AuthenticationException ex,
            HttpServletRequest request) {

        log.error("Authentication failed: {} - Path: {}", ex.getMessage(), request.getRequestURI());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    /**
     * Handles MethodArgumentNotValidException (400 BAD REQUEST).
     * 
     * Thrown when:
     * - Request body validation fails (@Valid annotation)
     * - Missing required fields
     * - Invalid field formats (e.g., invalid email)
     * - Field constraints violated (e.g., password too short)
     * 
     * Response: 400 BAD REQUEST
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
     * Validation Errors Include:
     * - Field name
     * - Validation constraint that failed
     * - Human-readable error message
     * 
     * @param ex      the MethodArgumentNotValidException
     * @param request the HTTP request
     * @return error response with 400 status and validation details
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        log.warn("Validation failed - Path: {} - Errors: {}",
                request.getRequestURI(),
                ex.getBindingResult().getFieldErrorCount());

        // Extract field validation errors
        List<String> validationErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::formatFieldError)
                .collect(Collectors.toList());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message("Validation failed")
                .path(request.getRequestURI())
                .validationErrors(validationErrors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handles generic exceptions (500 INTERNAL SERVER ERROR).
     * 
     * Catches all unexpected exceptions that weren't handled by
     * specific exception handlers.
     * 
     * Response: 500 INTERNAL SERVER ERROR
     * {
     * "timestamp": "2025-10-31T10:15:30",
     * "status": 500,
     * "error": "Internal Server Error",
     * "message": "An unexpected error occurred",
     * "path": "/api/v1/auth-service/auth/register"
     * }
     * 
     * Security Note:
     * Generic error message prevents leaking internal details.
     * Full exception details are logged for debugging.
     * 
     * @param ex      the generic exception
     * @param request the HTTP request
     * @return error response with 500 status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex,
            HttpServletRequest request) {

        log.error("Unexpected error occurred - Path: {} - Exception: {}",
                request.getRequestURI(),
                ex.getClass().getName(),
                ex);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message("An unexpected error occurred")
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    /**
     * Formats a field validation error into human-readable string.
     * 
     * Format: "fieldName: error message"
     * Example: "email: must be a well-formed email address"
     * 
     * @param error the field error
     * @return formatted error message
     */
    private String formatFieldError(FieldError error) {
        return String.format("%s: %s", error.getField(), error.getDefaultMessage());
    }
}
