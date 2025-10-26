package com.amsidh.mvc.orderservice.kafka;

import com.amsidh.mvc.kafka.order.OrderConfirmation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class OrderProducer {
  private final KafkaTemplate<String, OrderConfirmation> kafkaTemplate;

  public void sendOrderConfirmation(OrderConfirmation orderConfirmation) {
    log.info("Sending Order confirmation for order reference: {}", orderConfirmation.orderReference());
    Message<OrderConfirmation> message =
            MessageBuilder
                    .withPayload(orderConfirmation)
                    .setHeader(KafkaHeaders.TOPIC, "order-topic")
                    .setHeader("X-Trace-ID", MDC.get("traceId"))
                    .setHeader("X-Correlation-ID", MDC.get("traceId"))
                    .build();
    log.info("Order confirmation message built: {}", message);
    kafkaTemplate.send(message);
  }
}
