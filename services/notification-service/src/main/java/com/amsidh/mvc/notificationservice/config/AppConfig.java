package com.amsidh.mvc.notificationservice.config;

import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Map;

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


    //THis is done because EMailService is @Async and to propagate
    // the MDC context we need this
    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("async-");
        executor.setTaskDecorator(new MDCTaskDecorator());
        executor.initialize();
        return executor;
    }

    public static class MDCTaskDecorator implements TaskDecorator {
        @Override
        public Runnable decorate(Runnable runnable) {
            Map<String, String> contextMap = MDC.getCopyOfContextMap();
            return () -> {
                try {
                    MDC.setContextMap(contextMap);
                    runnable.run();
                } finally {
                    MDC.clear();
                }
            };
        }
    }
}
