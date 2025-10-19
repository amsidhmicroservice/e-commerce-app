package com.amsidh.mvc.orderservice.client.payment;

import com.amsidh.mvc.orderservice.dto.PaymentRequest;
import com.amsidh.mvc.orderservice.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PaymentServiceClientFallback {
    // Fallback method for createPayment with Throwable
    Integer createPayment(PaymentRequest paymentRequest, Throwable throwable) {
        // Log the exception (optional)
        log.error("PaymentServiceClientFallback: createPayment fallback triggered. {}", throwable.getMessage(), throwable);
        throw new BusinessException("Payment Service is currently unavailable. Please try again later.");
    }
}
