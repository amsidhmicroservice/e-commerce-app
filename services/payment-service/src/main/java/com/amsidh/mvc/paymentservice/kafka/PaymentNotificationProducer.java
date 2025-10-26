package com.amsidh.mvc.paymentservice.kafka;

import com.amsidh.mvc.kafka.payment.PaymentNotificationMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class PaymentNotificationProducer {
  private final KafkaTemplate<String, PaymentNotificationMessage> kafkaTemplate;

  public void sendPaymentNotification(PaymentNotificationMessage paymentNotificationMessage) {
    log.info("Sending Payment notification for payment reference: {}", paymentNotificationMessage.orderReference());
    Message<PaymentNotificationMessage> message =
            MessageBuilder
                    .withPayload(paymentNotificationMessage)
                    .setHeader(KafkaHeaders.TOPIC, "payment-topic")
                    .build();
    log.info("Payment notification message built: {}", message);
    kafkaTemplate.send(message);
  }
}
