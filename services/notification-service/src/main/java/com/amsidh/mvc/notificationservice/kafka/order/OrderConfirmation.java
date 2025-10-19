package com.amsidh.mvc.notificationservice.kafka.order;

import com.amsidh.mvc.notificationservice.util.PaymentMethod;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        Customer customer,
        List<Product> products
) {
}
