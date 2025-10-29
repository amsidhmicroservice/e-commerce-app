package com.amsidh.mvc.notificationservice.kafka.consumer;

import com.amsidh.mvc.kafka.order.OrderConfirmation;
import com.amsidh.mvc.kafka.payment.PaymentNotificationMessage;
import com.amsidh.mvc.notificationservice.entity.Notification;
import com.amsidh.mvc.notificationservice.repository.NotificationRepository;
import com.amsidh.mvc.notificationservice.service.EmailService;
import com.amsidh.mvc.notificationservice.util.NotificationMapper;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class NotificationConsumer {
    private final NotificationRepository notificationRepository;

    private final EmailService emailService;

    @KafkaListener(topics = "${notification-service.kafka.topic.order-confirmation}", groupId = "${notification-service.kafka.consumer.group-id.order-confirmation}")
    public void consumeOrderConfirmation(Message<OrderConfirmation> message) {
        propagateMDCContext(message);
        OrderConfirmation orderConfirmation = message.getPayload();
        log.info("Received Order Confirmation from Kafka - OrderRef: {}, Customer: {}, Amount: {}",
                orderConfirmation.orderReference(),
                orderConfirmation.customerResponse().email(),
                orderConfirmation.totalAmount());

        // Save the order confirmation to the database
        final Notification orderConfirmationNotification = NotificationMapper.toNotification(orderConfirmation);
        notificationRepository.save(orderConfirmationNotification);
        log.debug("Saved order confirmation notification to database with ID: {}",
                orderConfirmationNotification.getId());

        String customerName = orderConfirmation.customerResponse().firstName() + " "
                + orderConfirmation.customerResponse().lastName();
        try {
            log.info("Sending order confirmation email to: {} for order: {}",
                    orderConfirmation.customerResponse().email(),
                    orderConfirmation.orderReference());
            emailService.sendOrderConfirmationEmail(
                    orderConfirmation.customerResponse().email(),
                    customerName,
                    orderConfirmation.totalAmount(),
                    orderConfirmation.orderReference(),
                    orderConfirmation.products());
            log.info("Successfully sent order confirmation email to: {}", orderConfirmation.customerResponse().email());
        } catch (MessagingException e) {
            log.error("Failed to send order confirmation email to {} for order {} - MessagingException: {}",
                    orderConfirmation.customerResponse().email(),
                    orderConfirmation.orderReference(),
                    e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            log.error("Invalid email address for order {} - Error: {}",
                    orderConfirmation.orderReference(),
                    e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error sending order confirmation email to {} for order {} - Error: {}",
                    orderConfirmation.customerResponse().email(),
                    orderConfirmation.orderReference(),
                    e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "${notification-service.kafka.topic.payment-confirmation}", groupId = "${notification-service.kafka.consumer.group-id.payment-confirmation}")
    public void consumePaymentSuccessNotification(Message<PaymentNotificationMessage> message) {
        propagateMDCContext(message);
        PaymentNotificationMessage paymentNotificationMessage = message.getPayload();
        log.info("Received Payment Confirmation from Kafka - OrderRef: {}, Customer: {}, Amount: {}",
                paymentNotificationMessage.orderReference(),
                paymentNotificationMessage.customerEmailId(),
                paymentNotificationMessage.amount());

        // Save the payment confirmation to the database
        final Notification paymentConfirmationNotification = NotificationMapper
                .toNotification(paymentNotificationMessage);
        notificationRepository.save(paymentConfirmationNotification);
        log.debug("Saved payment confirmation notification to database with ID: {}",
                paymentConfirmationNotification.getId());

        String customerName = paymentNotificationMessage.customerFirstName() + " "
                + paymentNotificationMessage.customerLastName();
        try {
            log.info("Sending payment success email to: {} for order: {}",
                    paymentNotificationMessage.customerEmailId(),
                    paymentNotificationMessage.orderReference());
            emailService.sendPaymentSuccessEmail(
                    paymentNotificationMessage.customerEmailId(),
                    customerName,
                    paymentNotificationMessage.amount(),
                    paymentNotificationMessage.orderReference());
            log.info("Successfully sent payment success email to: {}", paymentNotificationMessage.customerEmailId());
        } catch (MessagingException e) {
            log.error("Failed to send payment confirmation email to {} for order {} - MessagingException: {}",
                    paymentNotificationMessage.customerEmailId(),
                    paymentNotificationMessage.orderReference(),
                    e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            log.error("Invalid email address for payment confirmation - order {} - Error: {}",
                    paymentNotificationMessage.orderReference(),
                    e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error sending payment confirmation email to {} for order {} - Error: {}",
                    paymentNotificationMessage.customerEmailId(),
                    paymentNotificationMessage.orderReference(),
                    e.getMessage(), e);
        }
    }

    private void propagateMDCContext(Message<?> message) {
        // Extract headers and set MDC for distributed tracing
        String traceId = (String) message.getHeaders().get("X-Trace-ID");
        String correlationId = (String) message.getHeaders().get("X-Correlation-ID");
        if (traceId != null) {
            MDC.put("traceId", traceId);
            log.debug("Propagated traceId from Kafka message: {}", traceId);
        }
        if (correlationId != null) {
            MDC.put("correlationId", correlationId);
            log.debug("Propagated correlationId from Kafka message: {}", correlationId);
        }
    }

}
