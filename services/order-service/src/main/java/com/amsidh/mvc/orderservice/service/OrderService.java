package com.amsidh.mvc.orderservice.service;

import com.amsidh.mvc.dto.OrderRequest;
import com.amsidh.mvc.dto.OrderResponse;

import java.util.List;

public interface OrderService {
    Integer createOrder(OrderRequest orderRequest);

    List<OrderResponse> getAllOrders();

    OrderResponse getOrderById(Integer orderId);
}
