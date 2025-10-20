package com.amsidh.mvc.notificationservice.util;

import com.amsidh.mvc.dto.OrderConfirmation;
import com.amsidh.mvc.dto.PaymentNotificationMessage;
import com.amsidh.mvc.notificationservice.entity.Notification;

public class NotificationMapper {
    public static Notification toNotification(PaymentNotificationMessage paymentNotificationMessage) {
        return Notification.builder()
                .notificationType(NotificationType.PAYMENT_CONFIGURATION)
                .notificationDateTime(java.time.LocalDateTime.now())
                .paymentNotificationMessage(paymentNotificationMessage)
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
