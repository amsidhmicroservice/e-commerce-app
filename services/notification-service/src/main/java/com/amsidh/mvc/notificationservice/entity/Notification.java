package com.amsidh.mvc.notificationservice.entity;

import com.amsidh.mvc.notificationservice.kafka.order.OrderConfirmation;
import com.amsidh.mvc.notificationservice.kafka.payment.PaymentConfirmation;
import com.amsidh.mvc.notificationservice.util.NotificationType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private PaymentConfirmation paymentConfirmation;

}
