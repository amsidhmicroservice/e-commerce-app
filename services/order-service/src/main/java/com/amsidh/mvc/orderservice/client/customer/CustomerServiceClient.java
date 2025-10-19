package com.amsidh.mvc.orderservice.client.customer;

import com.amsidh.mvc.orderservice.dto.CustomerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

/**
 * This is a Feign Client interface for communicating with the Customer Service.
 * So configure it as per your Customer Service details.
 */

/**
 * I have defined url configuration in order-service.yaml file like below
 * application:
 * config:
 * customer-service:
 * # This should be customer-service gateway url
 * url: http://localhost:8090/api/v1/customers
 * So you can use ${application.config.customer-service.url} in the url attribute of @FeignClient annotation
 */
@FeignClient(
        name = "customer-service",
        url = "${application.config.customer-service.url}",
        fallback = CustomerServiceClientFallback.class
)
public interface CustomerServiceClient {
    @GetMapping(path = "/api/v1/customers/{customer-id}")
    Optional<CustomerResponse> findCustomerById(@PathVariable("customer-id") String customerId);
}
