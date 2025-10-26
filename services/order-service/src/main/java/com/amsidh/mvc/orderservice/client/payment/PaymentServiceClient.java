package com.amsidh.mvc.orderservice.client.payment;

import com.amsidh.mvc.orderservice.dto.PaymentRequest;
import com.amsidh.mvc.orderservice.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class PaymentServiceClient {
    @Value("${application.config.payment-service.url}")
    private String paymentServiceUrl;
    private final RestTemplate restTemplate;

    public Integer createPayment(@RequestBody PaymentRequest paymentRequest) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        final HttpEntity<PaymentRequest> httpRequestEntity = new HttpEntity<>(paymentRequest, httpHeaders);
        ResponseEntity<Integer> response = restTemplate.exchange(
                paymentServiceUrl,
                HttpMethod.POST,
                httpRequestEntity,
                new ParameterizedTypeReference<>() {
                }
        );
        if (response.getStatusCode().isError()) {
            throw new BusinessException("Error occurred while purchasing products from payment-service: " + response.getStatusCode());
        }
        return response.getBody();
    }
}