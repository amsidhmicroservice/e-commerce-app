package com.amsidh.mvc.paymentservice.util;

import com.amsidh.mvc.kafka.payment.PaymentNotificationMessage;
import com.amsidh.mvc.paymentservice.dto.PaymentRequest;
import com.amsidh.mvc.paymentservice.entity.Payment;

public interface PaymentMapper {
    static Payment toPayment(PaymentRequest paymentRequest) {
        return Payment.builder()
                .amount(paymentRequest.amount())
                .paymentMethod(paymentRequest.paymentMethod())
                .orderId(paymentRequest.orderId())
                .build();
    }

    static PaymentNotificationMessage toPaymentNotificationMessage(PaymentRequest paymentRequest) {
        // Validate customer response is not null
        if (paymentRequest.customerResponse() == null) {
            throw new IllegalArgumentException("Customer response cannot be null for payment notification");
        }

        return new PaymentNotificationMessage(
                paymentRequest.orderReference(),
                paymentRequest.amount(),
                paymentRequest.paymentMethod(),
                paymentRequest.customerResponse().firstName(),
                paymentRequest.customerResponse().lastName(),
                paymentRequest.customerResponse().email());
    }
}
