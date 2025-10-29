package com.amsidh.mvc.customerservice.service;

import com.amsidh.mvc.customerservice.dto.CreateCustomerRequest;
import com.amsidh.mvc.customerservice.dto.CreateCustomerResponse;

import java.util.List;

/**
 * Service interface for customer operations.
 * Defines methods for creating, updating, retrieving, and deleting customers.
 */
public interface CustomerService {
    /**
     * Creates a new customer.
     * 
     * @param createCustomerRequest the customer creation request
     * @return success message with customer ID
     */
    String createCustomer(CreateCustomerRequest createCustomerRequest);

    /**
     * Updates an existing customer.
     * 
     * @param createCustomerRequest the customer update request
     */
    void updateCustomer(CreateCustomerRequest createCustomerRequest);

    /**
     * Retrieves all customers.
     * 
     * @return list of all customers
     */
    List<CreateCustomerResponse> getAllCustomers();

    /**
     * Checks if a customer exists by ID.
     * 
     * @param customerId the customer ID
     * @return true if customer exists, false otherwise
     */
    Boolean existsById(String customerId);

    /**
     * Finds a customer by ID.
     * 
     * @param customerId the customer ID
     * @return the customer response
     */
    CreateCustomerResponse findById(String customerId);

    /**
     * Deletes a customer by ID.
     * 
     * @param customerId the customer ID
     */
    void deleteById(String customerId);
}
