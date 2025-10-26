package com.amsidh.mvc.orderservice.dto;

import com.amsidh.mvc.kafka.order.CustomerResponse;
import com.amsidh.mvc.kafka.order.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        String orderReference,
        CustomerResponse customerResponse

) {
}
