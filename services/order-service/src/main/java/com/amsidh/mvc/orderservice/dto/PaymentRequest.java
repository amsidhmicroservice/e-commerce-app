package com.amsidh.mvc.orderservice.dto;

import com.amsidh.mvc.orderservice.util.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(BigDecimal amount, PaymentMethod paymentMethod, Integer orderId,
                             String orderReference, CustomerResponse customer

) {
}
