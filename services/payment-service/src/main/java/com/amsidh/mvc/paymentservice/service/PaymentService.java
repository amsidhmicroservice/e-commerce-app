package com.amsidh.mvc.paymentservice.service;

import com.amsidh.mvc.paymentservice.dto.PaymentRequest;

public interface PaymentService {
    Integer createPayment(PaymentRequest paymentRequest);
}
