package com.amsidh.mvc.dto;

import com.amsidh.mvc.util.PaymentMethod;

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
