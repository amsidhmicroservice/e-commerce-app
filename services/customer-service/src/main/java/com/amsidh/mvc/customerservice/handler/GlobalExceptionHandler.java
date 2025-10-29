package com.amsidh.mvc.customerservice.handler;

import com.amsidh.mvc.customerservice.exception.CustomerNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for the Customer Service.
 * Handles specific exceptions and provides appropriate HTTP responses.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

        /**
         * Handles CustomerNotFoundException.
         * 
         * @param customerNotFoundException the exception thrown when customer is not
         *                                  found
         * @return ResponseEntity with NOT_FOUND status and error message
         */
        @ExceptionHandler(CustomerNotFoundException.class)
        public ResponseEntity<String> handleCustomerNotFoundException(
                        CustomerNotFoundException customerNotFoundException) {
                log.error("Customer not found: {}", customerNotFoundException.getMessage());
                return ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(customerNotFoundException.getMessage());
        }

        /**
         * Handles validation errors from request body validation.
         * 
         * @param methodArgumentNotValidException the exception thrown when validation
         *                                        fails
         * @return ResponseEntity with BAD_REQUEST status and field-specific error
         *         messages
         */
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
                        MethodArgumentNotValidException methodArgumentNotValidException) {
                log.error("Validation failed with {} errors", methodArgumentNotValidException.getErrorCount());
                var errors = methodArgumentNotValidException.getAllErrors().stream()
                                .collect(
                                                java.util.stream.Collectors.toMap(
                                                                error -> ((FieldError) error).getField(),
                                                                DefaultMessageSourceResolvable::getDefaultMessage,
                                                                (existing, replacement) -> existing + "; "
                                                                                + replacement));
                log.debug("Validation errors: {}", errors);
                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(new ErrorResponse(errors));
        }
}
