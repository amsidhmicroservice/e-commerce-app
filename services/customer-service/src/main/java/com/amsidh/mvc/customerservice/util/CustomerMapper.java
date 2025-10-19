package com.amsidh.mvc.customerservice.util;

import com.amsidh.mvc.customerservice.dto.CustomerRequest;
import com.amsidh.mvc.customerservice.dto.CustomerResponse;
import com.amsidh.mvc.customerservice.entity.Customer;

public class CustomerMapper {
    public static Customer toCustomer(CustomerRequest customerRequest) {
        if (customerRequest == null)
            return null;
        return Customer.builder()
                .id(customerRequest.id())
                .firstName(customerRequest.firstName())
                .lastName(customerRequest.lastName())
                .email(customerRequest.email())
                .address(customerRequest.address())
                .build();
    }

    public static CustomerRequest toCustomerCreateRequest(Customer customer) {
        if (customer == null)
            return null;

        return new CustomerRequest(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getAddress());
    }


    public static CustomerResponse toCustomerResponse(Customer customer) {
        if (customer == null)
            return null;

        return new CustomerResponse(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getAddress());
    }
}
