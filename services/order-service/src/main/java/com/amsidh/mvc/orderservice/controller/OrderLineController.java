package com.amsidh.mvc.orderservice.controller;

import com.amsidh.mvc.dto.OrderLineResponse;
import com.amsidh.mvc.orderservice.service.OrderLineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/order-lines")
@Slf4j
@CrossOrigin(origins = "*")
public class OrderLineController {
    private final OrderLineService orderLineService;

    @GetMapping("/order/{order-id}")
    public ResponseEntity<List<OrderLineResponse>> findByOrderId(@PathVariable("order-id") Integer orderId) {
        log.info("Received request to get order lines for order id: {}", orderId);
        return ResponseEntity.ok(orderLineService.findAllByOrderId(orderId));
    }

}
