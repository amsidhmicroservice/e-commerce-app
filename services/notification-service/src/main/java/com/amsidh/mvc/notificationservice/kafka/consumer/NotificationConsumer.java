package com.amsidh.mvc.notificationservice.kafka.consumer;


import com.amsidh.mvc.dto.OrderConfirmation;
import com.amsidh.mvc.dto.PaymentNotificationMessage;
import com.amsidh.mvc.notificationservice.entity.Notification;
import com.amsidh.mvc.notificationservice.repository.NotificationRepository;
import com.amsidh.mvc.notificationservice.service.EmailService;
import com.amsidh.mvc.notificationservice.util.NotificationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class NotificationConsumer {
    private final NotificationRepository notificationRepository;

    private final EmailService emailService;

    @KafkaListener(topics = "${notification-service.kafka.topic.order-confirmation}", groupId = "${notification-service.kafka.consumer.group-id.order-confirmation}")
    public void consumeOrderConfirmation(OrderConfirmation orderConfirmation) {
        log.info("Received Order Confirmation message: {}", orderConfirmation);
        // Add logic to send notification for order confirmation
        //Save the order confirmation to the database
        final Notification orderConfirmationNotification = NotificationMapper.toNotification(orderConfirmation);
        notificationRepository.save(orderConfirmationNotification);

        String customerName = orderConfirmation.customerResponse().firstName() + " " + orderConfirmation.customerResponse().lastName();
        try {
            emailService.sendOrderConfirmationEmail(
                    orderConfirmation.customerResponse().email(),
                    customerName,
                    orderConfirmation.totalAmount(),
                    orderConfirmation.orderReference(),
                    orderConfirmation.products()
            );
        } catch (Exception e) {
            log.error("Failed to send order confirmation email to {} with error message {}", orderConfirmation.customerResponse().email(), e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "${notification-service.kafka.topic.payment-confirmation}", groupId = "${notification-service.kafka.consumer.group-id.payment-confirmation}")
    public void consumePaymentSuccessNotification(PaymentNotificationMessage paymentNotificationMessage) {
        log.info("Received Payment Confirmation: {}", paymentNotificationMessage);
        // Add logic to send notification for payment confirmation
        //Save the payment confirmation to the database
        final Notification paymentConfirmationNotification = NotificationMapper.toNotification(paymentNotificationMessage);
        notificationRepository.save(paymentConfirmationNotification);

        String customerName = paymentNotificationMessage.customerFirstName() + " " + paymentNotificationMessage.customerLastName();
        try {
            emailService.sendPaymentSuccessEmail(
                    paymentNotificationMessage.customerEmailId(),
                    customerName,
                    paymentNotificationMessage.amount(),
                    paymentNotificationMessage.orderReference()
            );
        } catch (Exception e) {
            log.error("Failed to send payment confirmation email to {} with error message {}", paymentNotificationMessage.customerEmailId(), e.getMessage(), e);
        }

    }


}
