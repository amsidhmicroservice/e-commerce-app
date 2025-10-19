package com.amsidh.mvc.customerservice.dto;

import com.amsidh.mvc.customerservice.entity.Address;

public record CustomerResponse(
        String id,

        String firstName,
        String lastName,

        String email,
        Address address) {
}
