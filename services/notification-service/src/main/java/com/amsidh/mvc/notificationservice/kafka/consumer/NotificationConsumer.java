package com.amsidh.mvc.notificationservice.kafka.consumer;


import com.amsidh.mvc.notificationservice.entity.Notification;
import com.amsidh.mvc.notificationservice.kafka.order.OrderConfirmation;
import com.amsidh.mvc.notificationservice.kafka.payment.PaymentConfirmation;
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

        String customerName = orderConfirmation.customer().firstName() + " " + orderConfirmation.customer().lastName();
        try {
            emailService.sendOrderConfirmationEmail(
                    orderConfirmation.customer().email(),
                    customerName,
                    orderConfirmation.totalAmount(),
                    orderConfirmation.orderReference(),
                    orderConfirmation.products()
            );
        } catch (Exception e) {
            log.error("Failed to send order confirmation email to {} with error message {}", orderConfirmation.customer().email(), e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "${notification-service.kafka.topic.payment-confirmation}", groupId = "${notification-service.kafka.consumer.group-id.payment-confirmation}")
    public void consumePaymentSuccessNotification(PaymentConfirmation paymentConfirmation) {
        log.info("Received Payment Confirmation: {}", paymentConfirmation);
        // Add logic to send notification for payment confirmation
        //Save the payment confirmation to the database
        final Notification paymentConfirmationNotification = NotificationMapper.toNotification(paymentConfirmation);
        notificationRepository.save(paymentConfirmationNotification);

        String customerName = paymentConfirmation.customerFirstName() + " " + paymentConfirmation.customerLastName();
        try {
            emailService.sendPaymentSuccessEmail(
                    paymentConfirmation.customerEmailId(),
                    customerName,
                    paymentConfirmation.amount(),
                    paymentConfirmation.orderReference()
            );
        } catch (Exception e) {
            log.error("Failed to send payment confirmation email to {} with error message {}", paymentConfirmation.customerEmailId(), e.getMessage(), e);
        }

    }


}
