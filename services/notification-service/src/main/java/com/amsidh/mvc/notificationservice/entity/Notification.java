package com.amsidh.mvc.notificationservice.entity;

import com.amsidh.mvc.kafka.order.OrderConfirmation;
import com.amsidh.mvc.kafka.payment.PaymentNotificationMessage;
import com.amsidh.mvc.notificationservice.util.NotificationType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Notification entity representing a notification sent to a customer.
 * Stored in MongoDB collection "notifications".
 * Can represent either an order confirmation or payment notification.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Document(collection = "notifications")
public class Notification {
    @Id
    private String id;
    private NotificationType notificationType;
    private LocalDateTime notificationDateTime;
    private OrderConfirmation orderConfirmation;
    private PaymentNotificationMessage paymentNotificationMessage;

}
