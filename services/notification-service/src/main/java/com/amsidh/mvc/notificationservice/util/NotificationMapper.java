package com.amsidh.mvc.notificationservice.util;

import com.amsidh.mvc.notificationservice.entity.Notification;
import com.amsidh.mvc.notificationservice.kafka.order.OrderConfirmation;
import com.amsidh.mvc.notificationservice.kafka.payment.PaymentConfirmation;

public class NotificationMapper {
    public static Notification toNotification(PaymentConfirmation paymentConfirmation) {
        return Notification.builder()
                .notificationType(NotificationType.PAYMENT_CONFIGURATION)
                .notificationDateTime(java.time.LocalDateTime.now())
                .paymentConfirmation(paymentConfirmation)
                .build();
    }

    public static Notification toNotification(OrderConfirmation orderConfirmation) {
        return Notification.builder()
                .notificationType(NotificationType.ORDER_CONFIGURATION)
                .notificationDateTime(java.time.LocalDateTime.now())
                .orderConfirmation(orderConfirmation)
                .build();
    }
}
