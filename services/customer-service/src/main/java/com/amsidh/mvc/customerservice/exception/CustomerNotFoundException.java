package com.amsidh.mvc.customerservice.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Custom exception thrown when a customer is not found in the system.
 * Extends RuntimeException for unchecked exception handling.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CustomerNotFoundException extends RuntimeException {
    private final String message;

}
