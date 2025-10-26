package com.amsidh.mvc.notificationservice.service;

import com.amsidh.mvc.kafka.order.PurchaseResponse;
import jakarta.mail.MessagingException;

import java.math.BigDecimal;
import java.util.List;

public interface EmailService {
    void sendPaymentSuccessEmail(String destinationEmail, String customerName, BigDecimal amount, String reference) throws MessagingException;

    void sendOrderConfirmationEmail(String destinationEmail, String customerName, BigDecimal amount, String orderReference, List<PurchaseResponse> products) throws MessagingException;
}
