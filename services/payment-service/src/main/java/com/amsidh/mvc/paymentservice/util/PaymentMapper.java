package com.amsidh.mvc.paymentservice.util;

import com.amsidh.mvc.paymentservice.dto.PaymentRequest;
import com.amsidh.mvc.paymentservice.entity.Payment;
import com.amsidh.mvc.paymentservice.kafka.dto.PaymentNotificationMessage;

public interface PaymentMapper {
    static Payment toPayment(PaymentRequest paymentRequest) {
        return Payment.builder()
                .amount(paymentRequest.amount())
                .paymentMethod(paymentRequest.paymentMethod())
                .orderId(paymentRequest.orderId())
                .build();
    }

    static PaymentNotificationMessage toPaymentNotificationMessage(PaymentRequest paymentRequest) {
        return new PaymentNotificationMessage(
                paymentRequest.orderReference(),
                paymentRequest.amount(),
                paymentRequest.paymentMethod(),
                paymentRequest.customerResponse().firstName(),
                paymentRequest.customerResponse().lastName(),
                paymentRequest.customerResponse().email()
        );
    }
}
