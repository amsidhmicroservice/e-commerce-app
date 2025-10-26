package com.amsidh.mvc.paymentservice.dto;

import com.amsidh.mvc.kafka.payment.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        String orderReference,
        CustomerResponse customerResponse

) {
}
