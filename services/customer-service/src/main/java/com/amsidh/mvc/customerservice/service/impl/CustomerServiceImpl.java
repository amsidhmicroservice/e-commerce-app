package com.amsidh.mvc.customerservice.service.impl;

import com.amsidh.mvc.customerservice.dto.CustomerRequest;
import com.amsidh.mvc.customerservice.dto.CustomerResponse;
import com.amsidh.mvc.customerservice.entity.Customer;
import com.amsidh.mvc.customerservice.exception.CustomerNotFoundException;
import com.amsidh.mvc.customerservice.repository.CustomerRepository;
import com.amsidh.mvc.customerservice.service.CustomerService;
import com.amsidh.mvc.customerservice.util.CustomerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

@RequiredArgsConstructor
@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public String createCustomer(CustomerRequest customerRequest) {
        final Customer savedCustomer = customerRepository.save(CustomerMapper.toCustomer(customerRequest));
        return savedCustomer.getId() + " saved successfully";
    }

    @Override
    public void updateCustomer(CustomerRequest customerRequest) {
        final Customer customer = customerRepository.findById(customerRequest.id())
                .orElseThrow(() -> new CustomerNotFoundException(format("Customer not found with id: %s", customerRequest.id())));
        mergeCustomer(customerRequest, customer);
        customerRepository.save(customer);
        log.info("Customer updated successfully with id: {}", customer.getId());
    }

    @Override
    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll().stream().map(CustomerMapper::toCustomerResponse).toList();
    }

    @Override
    public Boolean existsById(String customerId) {
        return customerRepository.existsById(customerId);
    }

    @Override
    public CustomerResponse findById(String customerId) {
        return customerRepository.findById(customerId)
                .map(CustomerMapper::toCustomerResponse)
                .orElseThrow(() -> new CustomerNotFoundException(format("Customer not found with id: %s", customerId)));
    }

    @Override
    public void deleteById(String customerId) {
        customerRepository.deleteById(customerId);
    }

    private static void mergeCustomer(CustomerRequest customerRequest, Customer customer) {
        Optional.ofNullable(customerRequest.firstName()).ifPresent(customer::setFirstName);
        Optional.ofNullable(customerRequest.lastName()).ifPresent(customer::setLastName);
        Optional.ofNullable(customerRequest.email()).ifPresent(customer::setEmail);
        if (customerRequest.address() != null) {
            if (customer.getAddress() == null) {
                customer.setAddress(customerRequest.address());
            } else {
                Optional.ofNullable(customerRequest.address().getStreet()).ifPresent(customer.getAddress()::setStreet);
                Optional.ofNullable(customerRequest.address().getHouseNumber()).ifPresent(customer.getAddress()::setHouseNumber);
                Optional.ofNullable(customerRequest.address().getZipCode()).ifPresent(customer.getAddress()::setZipCode);
            }
        }
    }
}
