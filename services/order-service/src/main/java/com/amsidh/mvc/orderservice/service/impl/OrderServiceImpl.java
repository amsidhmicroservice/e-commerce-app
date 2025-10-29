package com.amsidh.mvc.orderservice.service.impl;

import com.amsidh.mvc.kafka.order.CustomerResponse;
import com.amsidh.mvc.kafka.order.OrderConfirmation;
import com.amsidh.mvc.kafka.order.PurchaseResponse;
import com.amsidh.mvc.orderservice.client.customer.CustomerServiceClient;
import com.amsidh.mvc.orderservice.client.payment.PaymentServiceClient;
import com.amsidh.mvc.orderservice.client.product.ProductServiceClient;
import com.amsidh.mvc.orderservice.dto.OrderRequest;
import com.amsidh.mvc.orderservice.dto.OrderResponse;
import com.amsidh.mvc.orderservice.dto.PaymentRequest;
import com.amsidh.mvc.orderservice.entity.Order;
import com.amsidh.mvc.orderservice.exception.BusinessException;
import com.amsidh.mvc.orderservice.kafka.OrderProducer;
import com.amsidh.mvc.orderservice.repository.OrderRepository;
import com.amsidh.mvc.orderservice.service.OrderService;
import com.amsidh.mvc.orderservice.util.OrderMapper;
import com.amsidh.mvc.orderservice.util.OrderProducerMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final CustomerServiceClient customerServiceClient;
    private final ProductServiceClient productServiceClient;
    private final OrderRepository orderRepository;
    private final OrderLineServiceImpl orderLineServiceImpl;
    private final OrderProducer orderProducer;
    private final PaymentServiceClient paymentServiceClient;

    /**
     * Creates a new order with the following workflow:
     * 1. Validate customer exists
     * 2. Purchase products (deducts inventory)
     * 3. Save order and order lines
     * 4. Create payment transaction
     * 5. Publish order confirmation to Kafka
     * 
     * Note: This method is transactional for database operations only.
     * External service calls (customer, product, payment) are NOT part of the
     * transaction.
     * If payment or Kafka publishing fails after products are purchased,
     * a compensating transaction would be needed to restore product inventory.
     * Consider implementing Saga pattern for distributed transaction management.
     */
    @Transactional
    @Override
    public Integer createOrder(OrderRequest orderRequest) {
        log.info("Starting order creation process - Customer: {}, Products: {}",
                orderRequest.customerId(),
                orderRequest.productList().size());

        // Check the customer exists in customer-service microservice
        log.debug("Calling customer-service to validate customer: {}", orderRequest.customerId());
        final CustomerResponse customerResponse = customerServiceClient.findCustomerById(orderRequest.customerId())
                .orElseThrow(() -> {
                    log.error("Customer not found with id: {}", orderRequest.customerId());
                    return new BusinessException(
                            "Cannot create order:: No Customer exists with provided ID::" + orderRequest.customerId());
                });
        log.info("Customer validated - Email: {}, Name: {} {}",
                customerResponse.email(),
                customerResponse.firstName(),
                customerResponse.lastName());

        // Check the products are available in product-service microservice
        log.debug("Calling product-service to purchase {} products", orderRequest.productList().size());
        final List<PurchaseResponse> purchaseResponses = productServiceClient
                .purchaseProducts(orderRequest.productList());
        log.info("Products purchased successfully - Total items: {}", purchaseResponses.size());

        // Persist the order in order-database
        log.debug("Saving order to database");
        final Order savedOrder = orderRepository.save(OrderMapper.toOrder(orderRequest));
        log.info("Order saved with ID: {}, Reference: {}", savedOrder.getId(), savedOrder.getReference());

        // Persist the order lines in order-database
        log.debug("Saving {} order lines", orderRequest.productList().size());
        orderRequest.productList().forEach(purchaseRequest -> {
            final Integer savedOrderLineId = orderLineServiceImpl
                    .saveOrderLine(OrderMapper.toOrderLineRequest(purchaseRequest, savedOrder));
            log.debug("Saved order line - ID: {}, ProductID: {}, OrderID: {}",
                    savedOrderLineId,
                    purchaseRequest.productId(),
                    savedOrder.getId());
        });

        // Start payment transaction in payment-service microservice
        final PaymentRequest paymentRequest = OrderMapper.toPaymentRequest(orderRequest, savedOrder, customerResponse);
        log.info("Calling payment-service - Amount: {}, Method: {}, OrderRef: {}",
                paymentRequest.amount(),
                paymentRequest.paymentMethod(),
                paymentRequest.orderReference());
        final Integer payment = paymentServiceClient.createPayment(paymentRequest);
        log.info("Payment transaction created - Payment ID: {}", payment);

        // Send the order confirmation email using notification-service microservice
        // (async via Kafka)
        final OrderConfirmation orderConfirmation = OrderProducerMapper.toOrderConfirmation(orderRequest,
                customerResponse, purchaseResponses);
        log.debug("Publishing order confirmation to Kafka - OrderRef: {}", orderConfirmation.orderReference());
        orderProducer.sendOrderConfirmation(orderConfirmation);
        log.info("Order confirmation published to Kafka successfully");

        log.info("Order creation completed successfully - OrderID: {}, Reference: {}",
                savedOrder.getId(),
                savedOrder.getReference());
        return savedOrder.getId();
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        log.info("Retrieving all orders from database");
        List<OrderResponse> orders = orderRepository.findAll()
                .stream()
                .map(OrderMapper::toOrderResponse)
                .toList();
        log.info("Retrieved {} orders", orders.size());
        return orders;
    }

    @Override
    public OrderResponse getOrderById(Integer orderId) {
        log.info("Retrieving order with ID: {}", orderId);
        return orderRepository.findById(orderId)
                .map(order -> {
                    log.debug("Order found - ID: {}, Reference: {}", order.getId(), order.getReference());
                    return OrderMapper.toOrderResponse(order);
                })
                .orElseThrow(() -> {
                    log.error("Order not found with ID: {}", orderId);
                    return new EntityNotFoundException(String.format("No Order exists with provided ID:: %d", orderId));
                });
    }
}
