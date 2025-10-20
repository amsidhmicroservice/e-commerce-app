package com.amsidh.mvc.orderservice.util;

import com.amsidh.mvc.dto.OrderLineRequest;
import com.amsidh.mvc.dto.OrderLineResponse;
import com.amsidh.mvc.orderservice.entity.Order;
import com.amsidh.mvc.orderservice.entity.OrderLine;

import java.util.List;

public interface OrderLineMapper {

    static OrderLine toOrderLine(OrderLineRequest orderLineRequest) {
        return OrderLine.builder()
                .id(orderLineRequest.id())
                .order(Order.builder().id(orderLineRequest.orderId()).build())
                .productId(orderLineRequest.productId())
                .quantity(orderLineRequest.quantity())
                .build();
    }

    static List<OrderLineResponse> toOrderLineResponses(List<OrderLine> allByOrderId) {
        return allByOrderId.stream()
                .map(orderLine -> new OrderLineResponse(
                        orderLine.getId(),
                        orderLine.getQuantity()
                ))
                .toList();
    }
}
