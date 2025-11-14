package com.amsidh.mvc.authservice.exception;

/**
 * Exception thrown when authentication fails.
 * 
 * This exception is thrown in scenarios such as:
 * - Invalid credentials (wrong email or password)
 * - User not found
 * - Invalid or expired JWT token
 * - Token validation failures
 * 
 * HTTP Status: 401 UNAUTHORIZED
 * 
 * @author Amsidh Mohammed
 */
public class AuthenticationException extends RuntimeException {

    /**
     * Constructs a new authentication exception with the specified detail message.
     * 
     * @param message the detail message explaining the authentication failure
     */
    public AuthenticationException(String message) {
        super(message);
    }

    /**
     * Constructs a new authentication exception with the specified detail message
     * and cause.
     * 
     * @param message the detail message
     * @param cause   the cause of the exception
     */
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
