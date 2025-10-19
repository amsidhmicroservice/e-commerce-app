package com.amsidh.mvc.paymentservice.dto;

import com.amsidh.mvc.paymentservice.util.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
        Integer id,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        String orderReference,
        Customer customer

) {
}
