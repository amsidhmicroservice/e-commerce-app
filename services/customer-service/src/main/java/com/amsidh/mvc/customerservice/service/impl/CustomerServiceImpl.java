package com.amsidh.mvc.customerservice.service.impl;

import com.amsidh.mvc.customerservice.dto.CreateCustomerRequest;
import com.amsidh.mvc.customerservice.dto.CreateCustomerResponse;
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
    public String createCustomer(CreateCustomerRequest createCustomerRequest) {
        final Customer savedCustomer = customerRepository.save(CustomerMapper.toCustomer(createCustomerRequest));
        return savedCustomer.getId() + " saved successfully";
    }

    @Override
    public void updateCustomer(CreateCustomerRequest createCustomerRequest) {
        final Customer customer = customerRepository.findById(createCustomerRequest.id())
                .orElseThrow(() -> new CustomerNotFoundException(format("Customer not found with id: %s", createCustomerRequest.id())));
        mergeCustomer(createCustomerRequest, customer);
        customerRepository.save(customer);
        log.info("Customer updated successfully with id: {}", customer.getId());
    }

    @Override
    public List<CreateCustomerResponse> getAllCustomers() {
        return customerRepository.findAll().stream().map(CustomerMapper::toCustomerResponse).toList();
    }

    @Override
    public Boolean existsById(String customerId) {
        return customerRepository.existsById(customerId);
    }

    @Override
    public CreateCustomerResponse findById(String customerId) {
        return customerRepository.findById(customerId)
                .map(CustomerMapper::toCustomerResponse)
                .orElseThrow(() -> new CustomerNotFoundException(format("Customer not found with id: %s", customerId)));
    }

    @Override
    public void deleteById(String customerId) {
        customerRepository.deleteById(customerId);
    }

    private static void mergeCustomer(CreateCustomerRequest createCustomerRequest, Customer customer) {
        Optional.ofNullable(createCustomerRequest.firstName()).ifPresent(customer::setFirstName);
        Optional.ofNullable(createCustomerRequest.lastName()).ifPresent(customer::setLastName);
        Optional.ofNullable(createCustomerRequest.email()).ifPresent(customer::setEmail);
        if (createCustomerRequest.address() != null) {
            if (customer.getAddress() == null) {
                customer.setAddress(createCustomerRequest.address());
            } else {
                Optional.ofNullable(createCustomerRequest.address().getStreet()).ifPresent(customer.getAddress()::setStreet);
                Optional.ofNullable(createCustomerRequest.address().getHouseNumber()).ifPresent(customer.getAddress()::setHouseNumber);
                Optional.ofNullable(createCustomerRequest.address().getZipCode()).ifPresent(customer.getAddress()::setZipCode);
            }
        }
    }
}
