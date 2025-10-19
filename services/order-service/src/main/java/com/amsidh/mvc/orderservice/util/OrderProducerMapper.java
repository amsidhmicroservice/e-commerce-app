package com.amsidh.mvc.orderservice.util;

import com.amsidh.mvc.orderservice.dto.CustomerResponse;
import com.amsidh.mvc.orderservice.dto.OrderRequest;
import com.amsidh.mvc.orderservice.dto.PurchaseResponse;
import com.amsidh.mvc.orderservice.kafka.dto.OrderConfirmation;

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
