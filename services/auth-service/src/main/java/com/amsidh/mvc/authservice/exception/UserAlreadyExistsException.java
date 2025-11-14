package com.amsidh.mvc.authservice.exception;

/**
 * Exception thrown when attempting to register a user that already exists.
 * 
 * This exception is thrown during user registration when:
 * - Email address is already registered
 * - Username is already taken
 * 
 * HTTP Status: 409 CONFLICT
 * 
 * @author Amsidh Mohammed
 */
public class UserAlreadyExistsException extends RuntimeException {

    /**
     * Constructs a new user already exists exception with the specified detail
     * message.
     * 
     * @param message the detail message explaining which field already exists
     */
    public UserAlreadyExistsException(String message) {
        super(message);
    }

    /**
     * Constructs a new user already exists exception with the specified detail
     * message and cause.
     * 
     * @param message the detail message
     * @param cause   the cause of the exception
     */
    public UserAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
