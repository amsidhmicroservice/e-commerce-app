package com.amsidh.mvc.customerservice.service;

import com.amsidh.mvc.customerservice.dto.CreateCustomerRequest;
import com.amsidh.mvc.customerservice.dto.CreateCustomerResponse;

import java.util.List;

public interface CustomerService {
    String createCustomer(CreateCustomerRequest createCustomerRequest);

    void updateCustomer(CreateCustomerRequest createCustomerRequest);

    List<CreateCustomerResponse> getAllCustomers();

    Boolean existsById(String customerId);

    CreateCustomerResponse findById(String customerId);

    void deleteById(String customerId);
}
