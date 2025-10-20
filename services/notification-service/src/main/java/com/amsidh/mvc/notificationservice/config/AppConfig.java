package com.amsidh.mvc.notificationservice.config;

import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableConfigurationProperties(MailProperties.class)
@Slf4j
public class AppConfig {

    @Bean
    public JavaMailSender javaMailSender(MailProperties mailProperties) {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(mailProperties.getHost());
        sender.setPort(mailProperties.getPort());
        if (mailProperties.getUsername() != null) {
            sender.setUsername(mailProperties.getUsername());
            sender.setPassword(mailProperties.getPassword());
        }
        if (mailProperties.getProperties() != null) {
            sender.getJavaMailProperties().putAll(mailProperties.getProperties());
        }

        log.info("Configured JavaMailSender -> host='{}' port='{}' username='{}'",
                mailProperties.getHost(), mailProperties.getPort(),
                (mailProperties.getUsername() == null || mailProperties.getUsername().isEmpty()) ? "<none>"
                        : "<configured>");

        return sender;
    }
}
