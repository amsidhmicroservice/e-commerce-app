package com.amsidh.mvc.customerservice.util;

import com.amsidh.mvc.customerservice.dto.CreateCustomerRequest;
import com.amsidh.mvc.customerservice.dto.CreateCustomerResponse;
import com.amsidh.mvc.customerservice.entity.Customer;

public class CustomerMapper {
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
