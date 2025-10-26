package com.amsidh.mvc.paymentservice.dto;

public record CustomerResponse(
        String id,
        String firstName,
        String lastName,
        String email
) {
}
