package com.amsidh.mvc.dto;

import com.amsidh.mvc.util.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;

public record OrderRequest(
        Integer id,
        String reference,
        @Positive(message = "Amount must be positive")
        BigDecimal amount,
        @NotNull(message = "Payment method cannot be null")
        PaymentMethod paymentMethod,
        @NotNull(message = "CustomerResponse ID cannot be null")
        @NotEmpty(message = "CustomerResponse ID cannot be empty")
        @NotBlank(message = "CustomerResponse ID cannot be blank")
        String customerId,
        @NotEmpty(message = "Product list cannot be empty")
        List<PurchaseRequest> productList
) {
}
