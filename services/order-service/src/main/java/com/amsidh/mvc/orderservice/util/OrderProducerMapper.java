package com.amsidh.mvc.orderservice.util;


import com.amsidh.mvc.dto.CustomerResponse;
import com.amsidh.mvc.dto.OrderConfirmation;
import com.amsidh.mvc.dto.OrderRequest;
import com.amsidh.mvc.dto.PurchaseResponse;

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
