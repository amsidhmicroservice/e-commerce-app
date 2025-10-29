package com.amsidh.mvc.paymentservice.service.impl;

import com.amsidh.mvc.kafka.payment.PaymentNotificationMessage;
import com.amsidh.mvc.paymentservice.dto.PaymentRequest;
import com.amsidh.mvc.paymentservice.entity.Payment;
import com.amsidh.mvc.paymentservice.kafka.PaymentNotificationProducer;
import com.amsidh.mvc.paymentservice.repository.PaymentRepository;
import com.amsidh.mvc.paymentservice.service.PaymentService;
import com.amsidh.mvc.paymentservice.util.PaymentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentNotificationProducer paymentNotificationProducer;

    @Override
    public Integer createPayment(PaymentRequest paymentRequest) {
        log.info("Starting payment creation - Amount: {}, Method: {}, OrderRef: {}, Customer: {}",
                paymentRequest.amount(),
                paymentRequest.paymentMethod(),
                paymentRequest.orderReference(),
                paymentRequest.customerResponse() != null ? paymentRequest.customerResponse().email() : "N/A");

        // Convert request to entity
        final Payment payment = PaymentMapper.toPayment(paymentRequest);
        log.debug("Mapped payment request to entity - OrderRef: {}", payment.getOrderId());

        // Save payment to database
        final Payment savedPayment = paymentRepository.save(payment);
        log.info("Payment saved successfully - PaymentID: {}, Amount: {}, OrderRef: {}",
                savedPayment.getId(),
                savedPayment.getAmount(),
                savedPayment.getOrderId());

        // Prepare payment notification message
        final PaymentNotificationMessage paymentNotificationMessage = PaymentMapper
                .toPaymentNotificationMessage(paymentRequest);
        log.debug("Publishing payment notification to Kafka - OrderRef: {}, Customer: {}",
                paymentNotificationMessage.orderReference(),
                paymentNotificationMessage.customerEmailId());

        // Send notification via Kafka
        paymentNotificationProducer.sendPaymentNotification(paymentNotificationMessage);
        log.info("Payment notification published to Kafka successfully - PaymentID: {}", savedPayment.getId());

        return savedPayment.getId();
    }
}
