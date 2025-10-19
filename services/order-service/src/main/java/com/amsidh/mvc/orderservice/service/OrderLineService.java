package com.amsidh.mvc.orderservice.service;

import com.amsidh.mvc.orderservice.dto.OrderLineRequest;
import com.amsidh.mvc.orderservice.dto.OrderLineResponse;

import java.util.List;

public interface OrderLineService {
    Integer saveOrderLine(OrderLineRequest orderLineRequest);

    List<OrderLineResponse> findAllByOrderId(Integer orderId);
}
