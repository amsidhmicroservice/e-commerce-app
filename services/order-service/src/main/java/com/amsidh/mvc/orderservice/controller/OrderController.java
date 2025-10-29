package com.amsidh.mvc.orderservice.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amsidh.mvc.orderservice.dto.OrderRequest;
import com.amsidh.mvc.orderservice.dto.OrderResponse;
import com.amsidh.mvc.orderservice.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * REST controller for managing orders.
 * Provides endpoints for creating, retrieving orders.
 * 
 * This controller handles HTTP requests related to order operations,
 * including creating new orders and retrieving existing orders by ID or all
 * orders.
 */
// Lombok annotation to generate a constructor with required arguments
@RequiredArgsConstructor
// Marks this class as a Spring REST controller
@RestController
// Base path for all endpoints in this controller
@RequestMapping("/orders")
// Lombok annotation for logging
@Slf4j
// Allows cross-origin requests from any source
@CrossOrigin(origins = "*")
public class OrderController {

    // Injects the OrderService dependency using constructor injection
    private final OrderService orderService;

    /**
     * Endpoint to create a new order
     * 
     * @param orderRequest The order details to be created
     * @return ResponseEntity containing the ID of the created order
     */
    @PostMapping
    public ResponseEntity<Integer> createOrder(
            @RequestBody @Valid OrderRequest orderRequest) {
        log.info("Received request to create order - Customer: {}, Products count: {}, Payment Method: {}",
                orderRequest.customerId(),
                orderRequest.productList() != null ? orderRequest.productList().size() : 0,
                orderRequest.paymentMethod());
        Integer orderId = orderService.createOrder(orderRequest);
        log.info("Successfully created order with ID: {}", orderId);
        return ResponseEntity.ok().body(orderId);
    }

    /**
     * Endpoint to retrieve all orders
     * 
     * @return ResponseEntity containing a list of all orders
     */
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        log.info("Received request to retrieve all orders");
        List<OrderResponse> orders = orderService.getAllOrders();
        log.info("Retrieved {} orders", orders.size());
        return ResponseEntity.ok().body(orders);
    }

    /**
     * Endpoint to retrieve an order by its ID
     * 
     * @param orderId The ID of the order to retrieve
     * @return ResponseEntity containing the requested order
     */
    @GetMapping("/{order-id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable("order-id") Integer orderId) {
        log.info("Received request to retrieve order with ID: {}", orderId);
        OrderResponse order = orderService.getOrderById(orderId);
        log.info("Successfully retrieved order with ID: {}, Reference: {}", orderId, order.reference());
        return ResponseEntity.ok().body(order);
    }
}
