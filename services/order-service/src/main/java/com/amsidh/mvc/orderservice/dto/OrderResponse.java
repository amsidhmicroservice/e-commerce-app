package com.amsidh.mvc.orderservice.dto;


import com.amsidh.mvc.kafka.order.PaymentMethod;

import java.math.BigDecimal;

public record OrderResponse(
        Integer id,
        String reference,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        String customerId

) {
}
