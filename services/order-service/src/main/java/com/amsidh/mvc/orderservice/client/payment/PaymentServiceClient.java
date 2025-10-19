package com.amsidh.mvc.orderservice.client.payment;


import com.amsidh.mvc.orderservice.dto.PaymentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-service", url = "${application.config.payment-service.url}", fallback = PaymentServiceClientFallback.class)
public interface PaymentServiceClient {
    @PostMapping(produces = "application/json", consumes = "application/json")
    Integer createPayment(@RequestBody PaymentRequest paymentRequest);
}