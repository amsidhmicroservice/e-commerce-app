package com.amsidh.mvc.notificationservice.service.impl;

import com.amsidh.mvc.kafka.order.PurchaseResponse;
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

import static java.nio.charset.StandardCharsets.UTF_8;

@AllArgsConstructor
@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    @Override
    public void sendPaymentSuccessEmail(
            String destinationEmail,
            String customerName,
            BigDecimal amount,
            String orderReference
    ) throws MessagingException {
        // Implementation for sending email goes here
        final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        final MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, UTF_8.name());
        messageHelper.setFrom("contact@aliboucoding.com");
        final String templateName = EmailTemplate.PAYMENT_CONFIRMATION.getTemplate();

        Map<String, Object> variableMap = new HashMap<>();
        variableMap.put("customerName", customerName);
        variableMap.put("amount", amount);
        variableMap.put("orderReference", orderReference);

        Context context = new Context();
        context.setVariables(variableMap);
        messageHelper.setSubject(EmailTemplate.PAYMENT_CONFIRMATION.getSubject());
        try {
            log.info("Preparing Email content to send the email");
            final String htmlTemplate = templateEngine.process(templateName, context);
            messageHelper.setText(htmlTemplate, true);
            messageHelper.setTo(destinationEmail);
            javaMailSender.send(mimeMessage);
            log.info("EMAIL- Payment confirmation email sent to {} using template {}", destinationEmail, templateName);
        } catch (Exception e) {
            log.error("ERROR- Failed to send payment confirmation email to {} with error message {}", destinationEmail, e.getMessage(), e);
        }
    }

    @Async
    @Override
    public void sendOrderConfirmationEmail(
            String destinationEmail,
            String customerName,
            BigDecimal amount,
            String orderReference,
            List<PurchaseResponse> products
    ) throws MessagingException {
        // Implementation for sending email goes here
        final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        final MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_RELATED, StandardCharsets.UTF_8.name());
        messageHelper.setFrom("contact@aliboucoding.com");

        final String templateName = EmailTemplate.ORDER_CONFIRMATION.getTemplate();

        Map<String, Object> variableMap = new HashMap<>();
        variableMap.put("customerName", customerName);
        variableMap.put("totalAmount", amount);
        variableMap.put("orderReference", orderReference);
        variableMap.put("products", products);

        Context context = new Context();
        context.setVariables(variableMap);
        messageHelper.setSubject(EmailTemplate.ORDER_CONFIRMATION.getSubject());

        try {
            log.info("Inside try block sendOrderConfirmationEmail method");
            final String htmlTemplate = templateEngine.process(templateName, context);
            messageHelper.setText(htmlTemplate, true);
            messageHelper.setTo(destinationEmail);
            javaMailSender.send(mimeMessage);
            log.info("EMAIL- Order confirmation email sent to {} using template {}", destinationEmail, templateName);
        } catch (Exception e) {
            log.error("ERROR- Failed to send order confirmation email to {} with error message {}", destinationEmail, e.getMessage(), e);
        }
    }
}
