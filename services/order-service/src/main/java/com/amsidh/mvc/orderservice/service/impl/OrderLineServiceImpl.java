package com.amsidh.mvc.orderservice.service.impl;

import com.amsidh.mvc.dto.OrderLineRequest;
import com.amsidh.mvc.dto.OrderLineResponse;
import com.amsidh.mvc.orderservice.repository.OrderLineRepository;
import com.amsidh.mvc.orderservice.service.OrderLineService;
import com.amsidh.mvc.orderservice.util.OrderLineMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class OrderLineServiceImpl implements OrderLineService {

    private final OrderLineRepository orderLineRepository;

    @Override
    public Integer saveOrderLine(OrderLineRequest orderLineRequest) {
        return orderLineRepository.save(OrderLineMapper.toOrderLine(orderLineRequest)).getId();
    }

    @Override
    public List<OrderLineResponse> findAllByOrderId(Integer orderId) {
        return OrderLineMapper.toOrderLineResponses(orderLineRepository.findAllByOrderId(orderId));
    }
}
