package com.amsidh.mvc.paymentservice.service.impl;

import com.amsidh.mvc.paymentservice.dto.PaymentRequest;
import com.amsidh.mvc.paymentservice.entity.Payment;
import com.amsidh.mvc.paymentservice.kafka.PaymentNotificationProducer;
import com.amsidh.mvc.paymentservice.kafka.dto.PaymentNotificationMessage;
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
        log.info("Creating payment for request: {}", paymentRequest);
        final Payment payment = PaymentMapper
                .toPayment(paymentRequest);
        log.info("Saving payment entity: {}", payment);
        final Payment savedPayment = paymentRepository.save(payment);
        log.info("Payment saved successfully with id: {}", savedPayment.getId());


        final PaymentNotificationMessage paymentNotificationMessage = PaymentMapper.toPaymentNotificationMessage(paymentRequest);
        log.info("Sending payment notification message: {}", paymentNotificationMessage);
        paymentNotificationProducer.sendPaymentNotification(paymentNotificationMessage);
        return savedPayment.getId();
    }
}
