package com.amsidh.mvc.paymentservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public record CustomerResponse(
        String id,
        @NotNull(message = "First name must not be null")
        String firstName,
        @NotNull(message = "Last name must not be null")
        String lastName,
        @Email(message = "Email should be valid")
        @NotNull(message = "Email must not be null")
        String email
) {
}
