package com.amsidh.mvc.productservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductPurchaseRequest(
        @NotNull(message = "Product id is mandatory")
        Integer productId,
        @NotNull(message = "Product quantity is mandatory")
        @Positive(message = "Product quantity must be greater than zero")
        double quantity
) {
}
