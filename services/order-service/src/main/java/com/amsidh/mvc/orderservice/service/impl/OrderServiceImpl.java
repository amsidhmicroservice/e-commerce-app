package com.amsidh.mvc.orderservice.service.impl;

import com.amsidh.mvc.orderservice.client.customer.CustomerServiceClient;
import com.amsidh.mvc.orderservice.client.payment.PaymentServiceClient;
import com.amsidh.mvc.orderservice.client.product.ProductServiceClient;
import com.amsidh.mvc.orderservice.dto.*;
import com.amsidh.mvc.orderservice.entity.Order;
import com.amsidh.mvc.orderservice.exception.BusinessException;
import com.amsidh.mvc.orderservice.kafka.OrderProducer;
import com.amsidh.mvc.orderservice.kafka.dto.OrderConfirmation;
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

    @Transactional
    @Override
    public Integer createOrder(OrderRequest orderRequest) {
        //Check the customerResponse in customerResponse-service microservice
        final CustomerResponse customerResponse = customerServiceClient.findCustomerById(orderRequest.customerId()).orElseThrow(() -> {
            log.error("CustomerResponse with id {} not found", orderRequest.customerId());
            return new BusinessException("Cannot create order:: No CustomerResponse exists with provided ID::" + orderRequest.customerId());
        });

        //Check the product is available in product-service microservice
        final List<PurchaseResponse> purchaseResponses = productServiceClient.purchaseProducts(orderRequest.productList());
        log.info("Purchased products: {}", purchaseResponses);

        // persist the order in order-database
        final Order savedOrder = orderRepository.save(OrderMapper.toOrder(orderRequest));

        // persist the order line in order-database
        orderRequest.productList().forEach(purchaseRequest -> {
            final Integer savedOrderLineId = orderLineServiceImpl.saveOrderLine(OrderMapper.toOrderLineRequest(purchaseRequest, savedOrder));
            log.info("Saved order line id: {} for orderId {}", savedOrderLineId, savedOrder.getId());
        });

        // start payment transaction in payment-service microservice
        final PaymentRequest paymentRequest = OrderMapper.toPaymentRequest(orderRequest, savedOrder, customerResponse);
        log.info("Payment request: {}", paymentRequest);
        final Integer payment = paymentServiceClient.createPayment(paymentRequest);
        log.info("Payment transaction id: {}", payment);

        // send the order confirmation email using notification-service microservice(async kafka/rabbitmq broker)
        final OrderConfirmation orderConfirmation = OrderProducerMapper.toOrderConfirmation(orderRequest, customerResponse, purchaseResponses);
        orderProducer.sendOrderConfirmation(orderConfirmation);
        return savedOrder.getId();
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(OrderMapper::toOrderResponse)
                .toList();
    }

    @Override
    public OrderResponse getOrderById(Integer orderId) {
        return orderRepository.findById(orderId)
                .map(OrderMapper::toOrderResponse)
                .orElseThrow(() -> {
                    log.error("No Order exists with provided ID:: {}", orderId);
                    return new EntityNotFoundException(String.format("No Order exists with provided ID:: %d", orderId));
                });
    }
}
