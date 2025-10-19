package com.amsidh.mvc.notificationservice.kafka.payment;

import com.amsidh.mvc.notificationservice.util.PaymentMethod;

import java.math.BigDecimal;

public record PaymentConfirmation(
        String orderReference,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        String customerFirstName,
        String customerLastName,
        String customerEmailId
) {
}
