package com.amsidh.mvc.customerservice.util;

import com.amsidh.mvc.customerservice.dto.CreateCustomerRequest;
import com.amsidh.mvc.customerservice.dto.CreateCustomerResponse;
import com.amsidh.mvc.customerservice.entity.Customer;

/**
 * Utility class for mapping between Customer entities and DTOs.
 * Provides static methods for converting between different representations of
 * customer data.
 */
public class CustomerMapper {
    /**
     * Converts CreateCustomerRequest DTO to Customer entity.
     * 
     * @param createCustomerRequest the customer request DTO
     * @return Customer entity or null if request is null
     */
    public static Customer toCustomer(CreateCustomerRequest createCustomerRequest) {
        if (createCustomerRequest == null)
            return null;
        return Customer.builder()
                .id(createCustomerRequest.id())
                .firstName(createCustomerRequest.firstName())
                .lastName(createCustomerRequest.lastName())
                .email(createCustomerRequest.email())
                .address(createCustomerRequest.address())
                .build();
    }

    /**
     * Converts Customer entity to CreateCustomerRequest DTO.
     * 
     * @param customer the customer entity
     * @return CreateCustomerRequest DTO or null if customer is null
     */
    public static CreateCustomerRequest toCustomerCreateRequest(Customer customer) {
        if (customer == null)
            return null;

        return new CreateCustomerRequest(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getAddress());
    }

    /**
     * Converts Customer entity to CreateCustomerResponse DTO.
     * 
     * @param customer the customer entity
     * @return CreateCustomerResponse DTO or null if customer is null
     */
    public static CreateCustomerResponse toCustomerResponse(Customer customer) {
        if (customer == null)
            return null;

        return new CreateCustomerResponse(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getAddress());
    }
}
