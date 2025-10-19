package com.amsidh.mvc.customerservice.service;

import com.amsidh.mvc.customerservice.dto.CustomerRequest;
import com.amsidh.mvc.customerservice.dto.CustomerResponse;

import java.util.List;

public interface CustomerService {
    String createCustomer(CustomerRequest customerRequest);

    void updateCustomer(CustomerRequest customerRequest);

    List<CustomerResponse> getAllCustomers();

    Boolean existsById(String customerId);

    CustomerResponse findById(String customerId);

    void deleteById(String customerId);
}
