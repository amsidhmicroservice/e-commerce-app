package com.amsidh.mvc.orderservice.service;

import com.amsidh.mvc.orderservice.dto.OrderRequest;
import com.amsidh.mvc.orderservice.dto.OrderResponse;

import java.util.List;

public interface OrderService {
    Integer createOrder(OrderRequest orderRequest);

    List<OrderResponse> getAllOrders();

    OrderResponse getOrderById(Integer orderId);
}
