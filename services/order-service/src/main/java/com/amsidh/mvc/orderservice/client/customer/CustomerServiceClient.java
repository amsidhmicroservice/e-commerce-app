package com.amsidh.mvc.orderservice.client.customer;

import com.amsidh.mvc.kafka.order.CustomerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomerServiceClient {

    @Value("${application.config.customer-service.url}")
    private String customerServiceUrl;
    private final RestTemplate restTemplate;

    public Optional<CustomerResponse> findCustomerById(String customerId) {
        final CustomerResponse customerResponse = restTemplate.getForObject(customerServiceUrl + "/" + customerId,
                CustomerResponse.class);
        // Wrap in Optional, handling null case properly
        return Optional.ofNullable(customerResponse);
    }
}
