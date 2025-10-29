package com.amsidh.mvc.customerservice.handler;

import java.util.Map;

/**
 * Error response DTO for validation errors.
 * Contains a map of field names to error messages.
 *
 * @param errors Map of field names to their corresponding error messages
 */
public record ErrorResponse(
                Map<String, String> errors) {
}
