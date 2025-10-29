package com.amsidh.mvc.customerservice.dto;

import com.amsidh.mvc.customerservice.entity.Address;

/**
 * Data Transfer Object for customer response.
 * Contains customer information returned after creation or retrieval
 * operations.
 *
 * @param id        Customer unique identifier
 * @param firstName Customer's first name
 * @param lastName  Customer's last name
 * @param email     Customer's email address
 * @param address   Customer's physical address
 */
public record CreateCustomerResponse(
                String id,

                String firstName,
                String lastName,

                String email,
                Address address) {
}
