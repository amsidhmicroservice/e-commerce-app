package com.amsidh.mvc.orderservice.client.customer;

import com.amsidh.mvc.orderservice.dto.CustomerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(
        name = "customerResponse-service",
        url = "${application.config.customer-service.url}",
        fallback = CustomerServiceClientFallback.class
)
public interface CustomerServiceClient {
    @GetMapping(path = "/{customer-id}")
    Optional<CustomerResponse> findCustomerById(@PathVariable("customer-id") String customerId);
}
