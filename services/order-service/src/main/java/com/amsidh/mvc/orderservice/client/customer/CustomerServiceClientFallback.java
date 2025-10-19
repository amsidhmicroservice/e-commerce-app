package com.amsidh.mvc.orderservice.client.customer;

import com.amsidh.mvc.orderservice.dto.CustomerResponse;

import java.util.Optional;

public class CustomerServiceClientFallback implements CustomerServiceClient {

    @Override
    public Optional<CustomerResponse> findCustomerById(String customerId) {
        return Optional.empty();
    }
}
