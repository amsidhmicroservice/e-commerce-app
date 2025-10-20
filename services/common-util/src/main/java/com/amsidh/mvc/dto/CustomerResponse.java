package com.amsidh.mvc.dto;

public record CustomerResponse(
        String id,
        String firstName,
        String lastName,
        String email
) {
}
