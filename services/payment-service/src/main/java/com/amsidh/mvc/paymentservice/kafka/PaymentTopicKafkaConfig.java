package com.amsidh.mvc.paymentservice.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@Slf4j
public class PaymentTopicKafkaConfig {
    @Bean
    public NewTopic paymentTopic() {
        log.info("Creating payment-topic in Kafka broker");
        return TopicBuilder.name("payment-topic")
                .partitions(3)
                .replicas(1)
                .build();
    }

}
