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

/**
 * Implementation of CustomerService interface.
 * Handles all customer-related business logic including CRUD operations.
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    /**
     * Creates a new customer in the system.
     * 
     * @param createCustomerRequest the customer creation request
     * @return success message with customer ID
     */
    @Override
    public String createCustomer(CreateCustomerRequest createCustomerRequest) {
        log.info("Creating new customer with email: {}, firstName: {}, lastName: {}",
                createCustomerRequest.email(),
                createCustomerRequest.firstName(),
                createCustomerRequest.lastName());
        final Customer savedCustomer = customerRepository.save(CustomerMapper.toCustomer(createCustomerRequest));
        log.info("Customer created successfully with id: {}", savedCustomer.getId());
        return savedCustomer.getId() + " saved successfully";
    }

    /**
     * Updates an existing customer's information.
     * 
     * @param createCustomerRequest the customer update request
     * @throws CustomerNotFoundException if customer is not found
     */
    @Override
    public void updateCustomer(CreateCustomerRequest createCustomerRequest) {
        log.info("Updating customer with id: {}", createCustomerRequest.id());
        final Customer customer = customerRepository.findById(createCustomerRequest.id())
                .orElseThrow(() -> {
                    log.error("Customer not found with id: {}", createCustomerRequest.id());
                    return new CustomerNotFoundException(
                            format("Customer not found with id: %s", createCustomerRequest.id()));
                });
        log.debug("Found customer: {}, proceeding with update", customer.getId());
        mergeCustomer(createCustomerRequest, customer);
        customerRepository.save(customer);
        log.info("Customer updated successfully with id: {}", customer.getId());
    }

    /**
     * Retrieves all customers from the database.
     * 
     * @return list of all customer responses
     */
    @Override
    public List<CreateCustomerResponse> getAllCustomers() {
        log.info("Retrieving all customers from database");
        List<CreateCustomerResponse> customers = customerRepository.findAll()
                .stream()
                .map(CustomerMapper::toCustomerResponse)
                .toList();
        log.info("Retrieved {} customers", customers.size());
        return customers;
    }

    /**
     * Checks if a customer exists by ID.
     * 
     * @param customerId the customer ID
     * @return true if customer exists, false otherwise
     */
    @Override
    public Boolean existsById(String customerId) {
        log.info("Checking if customer exists with id: {}", customerId);
        Boolean exists = customerRepository.existsById(customerId);
        log.debug("Customer existence check for id {}: {}", customerId, exists);
        return exists;
    }

    /**
     * Finds a customer by ID.
     * 
     * @param customerId the customer ID
     * @return the customer response
     * @throws CustomerNotFoundException if customer is not found
     */
    @Override
    public CreateCustomerResponse findById(String customerId) {
        log.info("Retrieving customer with id: {}", customerId);
        return customerRepository.findById(customerId)
                .map(customer -> {
                    log.debug("Customer found with id: {}, email: {}", customer.getId(), customer.getEmail());
                    return CustomerMapper.toCustomerResponse(customer);
                })
                .orElseThrow(() -> {
                    log.error("Customer not found with id: {}", customerId);
                    return new CustomerNotFoundException(format("Customer not found with id: %s", customerId));
                });
    }

    /**
     * Deletes a customer by ID.
     * 
     * @param customerId the customer ID
     * @throws CustomerNotFoundException if customer is not found
     */
    @Override
    public void deleteById(String customerId) {
        log.info("Deleting customer with id: {}", customerId);
        // Check if customer exists before deletion
        if (!customerRepository.existsById(customerId)) {
            log.error("Cannot delete - Customer not found with id: {}", customerId);
            throw new CustomerNotFoundException(format("Customer not found with id: %s", customerId));
        }
        customerRepository.deleteById(customerId);
        log.info("Customer with id {} deleted successfully", customerId);
    }

    /**
     * Merges customer request data into existing customer entity.
     * Only updates non-null fields.
     * 
     * @param createCustomerRequest the customer request with updated data
     * @param customer              the existing customer entity
     */
    private static void mergeCustomer(CreateCustomerRequest createCustomerRequest, Customer customer) {
        Optional.ofNullable(createCustomerRequest.firstName()).ifPresent(customer::setFirstName);
        Optional.ofNullable(createCustomerRequest.lastName()).ifPresent(customer::setLastName);
        Optional.ofNullable(createCustomerRequest.email()).ifPresent(customer::setEmail);
        if (createCustomerRequest.address() != null) {
            if (customer.getAddress() == null) {
                customer.setAddress(createCustomerRequest.address());
            } else {
                Optional.ofNullable(createCustomerRequest.address().getStreet())
                        .ifPresent(customer.getAddress()::setStreet);
                Optional.ofNullable(createCustomerRequest.address().getHouseNumber())
                        .ifPresent(customer.getAddress()::setHouseNumber);
                Optional.ofNullable(createCustomerRequest.address().getZipCode())
                        .ifPresent(customer.getAddress()::setZipCode);
            }
        }
    }
}
