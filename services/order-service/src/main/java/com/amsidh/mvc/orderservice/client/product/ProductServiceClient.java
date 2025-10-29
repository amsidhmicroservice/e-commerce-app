package com.amsidh.mvc.orderservice.client.product;

import com.amsidh.mvc.kafka.order.PurchaseResponse;
import com.amsidh.mvc.orderservice.dto.PurchaseRequest;
import com.amsidh.mvc.orderservice.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductServiceClient {

    @Value("${application.config.product-service.url}")
    private String productServiceUrl;
    private final RestTemplate restTemplate;

    public List<PurchaseResponse> purchaseProducts(List<PurchaseRequest> purchaseRequestList) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));

        final HttpEntity<List<PurchaseRequest>> httpRequestEntity = new HttpEntity<>(purchaseRequestList, httpHeaders);
        ResponseEntity<List<PurchaseResponse>> response = restTemplate.exchange(
                productServiceUrl + "/purchase",
                HttpMethod.POST,
                httpRequestEntity,
                new ParameterizedTypeReference<>() {
                });
        if (response.getStatusCode().isError()) {
            throw new BusinessException(
                    "Error occurred while purchasing products from product-service: " + response.getStatusCode());
        }

        // Validate response body is not null
        if (response.getBody() == null) {
            throw new BusinessException("Product service returned null response body");
        }

        return response.getBody();
    }

}
