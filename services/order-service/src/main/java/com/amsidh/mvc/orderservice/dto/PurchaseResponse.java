package com.amsidh.mvc.orderservice.dto;

import java.math.BigDecimal;

public record PurchaseResponse(Integer id,
                               String name,
                               BigDecimal price,
                               double quantity
) {
}
