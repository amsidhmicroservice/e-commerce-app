package com.amsidh.mvc.orderservice.dto;

public record CustomerResponse(
        String id,
        String firstName,
        String lastName,
        String email
) {
}
