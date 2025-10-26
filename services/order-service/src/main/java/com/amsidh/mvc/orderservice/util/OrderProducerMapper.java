package com.amsidh.mvc.orderservice.util;


import com.amsidh.mvc.kafka.order.CustomerResponse;
import com.amsidh.mvc.kafka.order.OrderConfirmation;
import com.amsidh.mvc.kafka.order.PurchaseResponse;
import com.amsidh.mvc.orderservice.dto.OrderRequest;

import java.util.List;

public class OrderProducerMapper {
    public static OrderConfirmation toOrderConfirmation(OrderRequest orderRequest, CustomerResponse customerResponse, List<PurchaseResponse> purchaseResponses) {
        return new OrderConfirmation(
                orderRequest.reference(),
                orderRequest.amount(),
                orderRequest.paymentMethod(),
                customerResponse,
                purchaseResponses
        );
    }
}
