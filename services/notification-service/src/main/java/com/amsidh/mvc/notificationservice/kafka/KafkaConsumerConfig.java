package com.amsidh.mvc.notificationservice.kafka;

import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaConsumerConfig {

    @Value("${notification-service.kafka.dlq.suffix:.DLT}")
    private String dlqSuffix;

    @Bean
    public DefaultErrorHandler errorHandler(KafkaTemplate<Object, Object> kafkaTemplate) {
        DeadLetterPublishingRecoverer recover = new DeadLetterPublishingRecoverer(kafkaTemplate,
                (r, e) -> new TopicPartition(r.topic() + dlqSuffix, r.partition()));
        // Configure retries/backoff; here simple FixedBackOff (no retries)
        FixedBackOff fixedBackOff = new FixedBackOff(0L, 0L); // no retries, immediately send to DLQ

        return new DefaultErrorHandler(recover, fixedBackOff);
    }
}