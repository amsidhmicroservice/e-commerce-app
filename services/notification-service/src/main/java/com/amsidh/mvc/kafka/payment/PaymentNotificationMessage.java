package com.amsidh.mvc.kafka.payment;

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
