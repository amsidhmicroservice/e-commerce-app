package com.amsidh.mvc.orderservice.kafka.dto;

import com.amsidh.mvc.orderservice.dto.CustomerResponse;
import com.amsidh.mvc.orderservice.dto.PurchaseResponse;
import com.amsidh.mvc.orderservice.util.PaymentMethod;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customerResponse,
        List<PurchaseResponse> products
) {
}
