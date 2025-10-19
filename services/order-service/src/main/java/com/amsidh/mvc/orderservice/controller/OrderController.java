package com.amsidh.mvc.orderservice.controller;

import com.amsidh.mvc.orderservice.dto.OrderRequest;
import com.amsidh.mvc.orderservice.dto.OrderResponse;
import com.amsidh.mvc.orderservice.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
@Slf4j
public class OrderController {

    private final OrderService orderService;


    @PostMapping
    public ResponseEntity<Integer> createOrder(
            @RequestBody @Valid OrderRequest orderRequest
    ) {
        log.info("Received request to create order");
        return ResponseEntity.ok().body(orderService.createOrder(orderRequest));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        log.info("Received request to get all orders");
        return ResponseEntity.ok().body(orderService.getAllOrders());
    }

    @GetMapping("/{order-id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable("order-id") Integer orderId) {
        log.info("Received request to get all orders");
        return ResponseEntity.ok().body(orderService.getOrderById(orderId));
    }


}
