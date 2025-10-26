package com.amsidh.mvc.kafka.order;

public record CustomerResponse(
        String id,
        String firstName,
        String lastName,
        String email
) {
}
