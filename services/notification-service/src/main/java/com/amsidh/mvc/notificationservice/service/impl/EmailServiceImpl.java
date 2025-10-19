package com.amsidh.mvc.notificationservice.service.impl;

import com.amsidh.mvc.notificationservice.kafka.order.Product;
import com.amsidh.mvc.notificationservice.service.EmailService;
import com.amsidh.mvc.notificationservice.util.EmailTemplate;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    @Override
    public void sendPaymentSuccessEmail(String destinationEmail, String customerName, BigDecimal amount, String orderReference) throws MessagingException {
        // Implementation for sending email goes here
        final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        final MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_RELATED, StandardCharsets.UTF_8.name());
        mimeMessageHelper.setTo(destinationEmail);
        mimeMessageHelper.setFrom("xyz@gmail.com");
        mimeMessageHelper.setSubject(EmailTemplate.PAYMENT_CONFIRMATION.getSubject());

        Map<String, Object> variableMap = new HashMap<>();
        variableMap.put("customerName", customerName);
        variableMap.put("amount", amount);
        variableMap.put("reference", orderReference);
        Context context = new Context();
        context.setVariables(variableMap);
        try {
            String template = EmailTemplate.PAYMENT_CONFIRMATION.getTemplate();
            final String htmlContent = templateEngine.process(template, context);
            mimeMessageHelper.setText(htmlContent, true);
            javaMailSender.send(mimeMessage);
            log.info("Payment confirmation email sent to {} using template {}", destinationEmail, template);
        } catch (Exception e) {
            log.error("Failed to send payment confirmation email to {} with error message {}", destinationEmail, e.getMessage(), e);
        }
    }

    @Async
    @Override
    public void sendOrderConfirmationEmail(String destinationEmail, String customerName, BigDecimal amount, String orderReference, List<Product> products) throws MessagingException {
        // Implementation for sending email goes here
        final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        final MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_RELATED, StandardCharsets.UTF_8.name());
        mimeMessageHelper.setTo(destinationEmail);
        mimeMessageHelper.setFrom("xyz@gmail.com");
        mimeMessageHelper.setSubject(EmailTemplate.ORDER_CONFIRMATION.getSubject());

        Map<String, Object> variableMap = new HashMap<>();
        variableMap.put("customerName", customerName);
        variableMap.put("totalAmount", amount);
        variableMap.put("reference", orderReference);
        variableMap.put("products", products);
        Context context = new Context();
        context.setVariables(variableMap);
        try {
            String template = EmailTemplate.ORDER_CONFIRMATION.getTemplate();
            final String htmlContent = templateEngine.process(template, context);
            mimeMessageHelper.setText(htmlContent, true);
            javaMailSender.send(mimeMessage);
            log.info("Order confirmation email sent to {} using template {}", destinationEmail, template);
        } catch (Exception e) {
            log.error("Failed to send order confirmation email to {} with error message {}", destinationEmail, e.getMessage(), e);
        }
    }
}
