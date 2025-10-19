package com.amsidh.mvc.orderservice.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@Slf4j
public class OrderTopicKafkaConfig {

    @Bean
    public NewTopic orderTopic() {
        log.info("Creating order-topic in Kafka broker");
        //return new NewTopic("order-topic", 3, (short) 1);
        return TopicBuilder.name("order-topic")
                .partitions(3)
                .replicas(1)
                .build();
    }

}
