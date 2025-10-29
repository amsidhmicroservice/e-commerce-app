package com.amsidh.mvc.customerservice.dto;

import com.amsidh.mvc.customerservice.entity.Address;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object for creating a new customer.
 * Contains validated customer information including personal details and
 * address.
 *
 * @param id        Customer unique identifier
 * @param firstName Customer's first name (required)
 * @param lastName  Customer's last name (required)
 * @param email     Customer's email address (required, must be valid format)
 * @param address   Customer's physical address
 */
public record CreateCustomerRequest(
                String id,

                @NotNull(message = "First name cannot be null") String firstName,
                @NotNull(message = "Last name cannot be null") String lastName,

                @NotNull(message = "Email cannot be null") @Email(message = "Email should be valid") String email,
                Address address) {
}