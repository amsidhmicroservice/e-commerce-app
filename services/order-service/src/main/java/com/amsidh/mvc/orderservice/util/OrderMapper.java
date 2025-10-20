package com.amsidh.mvc.orderservice.util;

import com.amsidh.mvc.orderservice.dto.*;
import com.amsidh.mvc.orderservice.entity.Order;

public interface OrderMapper {

    static Order toOrder(OrderRequest orderRequest) {
        return Order.builder()
                .id(orderRequest.id())
                .reference(orderRequest.reference())
                .totalAmount(orderRequest.amount())
                .paymentMethod(orderRequest.paymentMethod())
                .customerId(orderRequest.customerId())
                .build();
    }

    static OrderLineRequest toOrderLineRequest(PurchaseRequest purchaseRequest, Order savedOrder) {
        return new OrderLineRequest(
                null,
                savedOrder.getId(),
                purchaseRequest.productId(),
                purchaseRequest.quantity()
        );
    }

    static OrderResponse toOrderResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getReference(),
                order.getTotalAmount(),
                order.getPaymentMethod(),
                order.getCustomerId()
        );
    }

    static PaymentRequest toPaymentRequest(OrderRequest orderRequest, Order savedOrder, CustomerResponse customer) {
        return new PaymentRequest(
                orderRequest.amount(),
                orderRequest.paymentMethod(),
                savedOrder.getId(),
                savedOrder.getReference(),
                customer
        );
    }
}
