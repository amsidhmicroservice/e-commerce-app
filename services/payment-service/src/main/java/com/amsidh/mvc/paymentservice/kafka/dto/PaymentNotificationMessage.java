package com.amsidh.mvc.paymentservice.kafka.dto;

import com.amsidh.mvc.paymentservice.util.PaymentMethod;

import java.math.BigDecimal;

public record PaymentNotificationMessage(
        String orderReference,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        String customerFirstName,
        String customerLastName,
        String customerEmailId
) {
}
