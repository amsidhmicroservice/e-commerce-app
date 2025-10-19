package com.amsidh.mvc.productservice.dto;

import java.math.BigDecimal;

public record ProductPurchaseResponse(
        Integer id,
        String name,
        BigDecimal price,
        double quantity
) {

}
