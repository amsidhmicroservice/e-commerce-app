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

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<String> createCustomer(@RequestBody CreateCustomerRequest createCustomerRequest) {
        log.info("Create Customer API is called");
        return ResponseEntity.ok(customerService.createCustomer(createCustomerRequest));
    }

    @PutMapping
    public ResponseEntity<Void> updateCustomer(@RequestBody @Valid CreateCustomerRequest createCustomerRequest) {
        log.info("Update customer with id {}", createCustomerRequest.id());
        customerService.updateCustomer(createCustomerRequest);
        log.info("Customer with id {} is updated successfully", createCustomerRequest.id());
        return ResponseEntity.accepted().build();
    }

    @GetMapping
    public List<CreateCustomerResponse> getAllCustomers() {
        log.info("Get all customers");
        return customerService.getAllCustomers();
    }

    @GetMapping("/exists/{customer-id}")
    public ResponseEntity<Boolean> existsById(
            @PathVariable(name = "customer-id") String customerId) {
        log.info("Check customer with id {} is exists", customerId);
        return ResponseEntity.ok(customerService.existsById(customerId));
    }

    @GetMapping("/{customer-id}")
    public ResponseEntity<CreateCustomerResponse> getCustomerById(
            @PathVariable(name = "customer-id") String customerId) {
        log.info("Get customer with id {}", customerId);
        return ResponseEntity.ok(customerService.findById(customerId));
    }

    @DeleteMapping("/{customer-id}")
    public ResponseEntity<Void> deleteCustomerById(
            @PathVariable(name = "customer-id") String customerId) {
        customerService.deleteById(customerId);
        return ResponseEntity.accepted().build();
    }
}
