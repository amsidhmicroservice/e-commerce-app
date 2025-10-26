package com.amsidh.mvc.kafka.order;

import java.math.BigDecimal;

public record PurchaseResponse(
        Integer id,
        String name,
        BigDecimal price,
        double quantity
) {
}
