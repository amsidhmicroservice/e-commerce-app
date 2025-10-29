package com.amsidh.mvc.customerservice.controller;

import com.amsidh.mvc.customerservice.dto.CreateCustomerRequest;
import com.amsidh.mvc.customerservice.dto.CreateCustomerResponse;
import com.amsidh.mvc.customerservice.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for customer operations.
 *
 * Provides endpoints to create, update, retrieve and delete customer
 * information.
 */
@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class CustomerController {

    // Injects CustomerService dependency
    private final CustomerService customerService;

    /**
     * Create a new customer.
     * 
     * @param createCustomerRequest request payload with customer details
     * @return id of created customer as string
     */
    @PostMapping
    public ResponseEntity<String> createCustomer(@RequestBody CreateCustomerRequest createCustomerRequest) {
        log.info("Received request to create customer with email: {}", createCustomerRequest.email());
        String result = customerService.createCustomer(createCustomerRequest);
        log.info("Successfully created customer: {}", result);
        return ResponseEntity.ok(result);
    }

    /**
     * Update existing customer details.
     * 
     * @param createCustomerRequest payload with updated customer information
     * @return HTTP 202 Accepted on success
     */
    @PutMapping
    public ResponseEntity<Void> updateCustomer(@RequestBody @Valid CreateCustomerRequest createCustomerRequest) {
        log.info("Received request to update customer with id: {}, email: {}", createCustomerRequest.id(),
                createCustomerRequest.email());
        customerService.updateCustomer(createCustomerRequest);
        log.info("Successfully updated customer with id: {}", createCustomerRequest.id());
        return ResponseEntity.accepted().build();
    }

    /**
     * Retrieve all customers.
     * 
     * @return list of customers
     */
    @GetMapping
    public List<CreateCustomerResponse> getAllCustomers() {
        log.info("Received request to retrieve all customers");
        List<CreateCustomerResponse> customers = customerService.getAllCustomers();
        log.info("Retrieved {} customers", customers.size());
        return customers;
    }

    /**
     * Check if a customer exists by id.
     * 
     * @param customerId id of the customer
     * @return boolean indicating existence
     */
    @GetMapping("/exists/{customer-id}")
    public ResponseEntity<Boolean> existsById(
            @PathVariable(name = "customer-id") String customerId) {
        log.info("Received request to check if customer exists with id: {}", customerId);
        boolean exists = customerService.existsById(customerId);
        log.info("Customer with id {} existence check result: {}", customerId, exists);
        return ResponseEntity.ok(exists);
    }

    /**
     * Retrieve a customer by id.
     * 
     * @param customerId id of the customer
     * @return customer details
     */
    @GetMapping("/{customer-id}")
    public ResponseEntity<CreateCustomerResponse> getCustomerById(
            @PathVariable(name = "customer-id") String customerId) {
        log.info("Received request to retrieve customer with id: {}", customerId);
        CreateCustomerResponse customer = customerService.findById(customerId);
        log.info("Successfully retrieved customer with id: {}, email: {}", customerId, customer.email());
        return ResponseEntity.ok(customer);
    }

    /**
     * Delete a customer by id.
     * 
     * @param customerId id of the customer to delete
     * @return HTTP 202 Accepted on success
     */
    @DeleteMapping("/{customer-id}")
    public ResponseEntity<Void> deleteCustomerById(
            @PathVariable(name = "customer-id") String customerId) {
        log.info("Received request to delete customer with id: {}", customerId);
        customerService.deleteById(customerId);
        log.info("Successfully deleted customer with id: {}", customerId);
        return ResponseEntity.accepted().build();
    }
}
