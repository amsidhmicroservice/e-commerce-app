package com.amsidh.mvc.orderservice.controller;

import com.amsidh.mvc.orderservice.dto.OrderLineResponse;
import com.amsidh.mvc.orderservice.service.OrderLineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for order line operations.
 *
 * Provides endpoint(s) to retrieve order lines related to an order.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/order-lines")
@Slf4j
@CrossOrigin(origins = "*")
public class OrderLineController {
    // Injects OrderLineService dependency
    private final OrderLineService orderLineService;

    /**
     * Retrieve order lines by order id.
     * 
     * @param orderId id of the order
     * @return list of order line responses for the given order
     */
    @GetMapping("/order/{order-id}")
    public ResponseEntity<List<OrderLineResponse>> findByOrderId(@PathVariable("order-id") Integer orderId) {
        log.info("Received request to get order lines for order id: {}", orderId);
        return ResponseEntity.ok(orderLineService.findAllByOrderId(orderId));
    }

}
