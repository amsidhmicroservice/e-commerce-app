package com.amsidh.mvc.paymentservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amsidh.mvc.paymentservice.dto.PaymentRequest;
import com.amsidh.mvc.paymentservice.service.PaymentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * REST controller for payment operations.
 *
 * Exposes endpoint(s) to create payments and handle payment-related requests.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/payments")
@Slf4j
@CrossOrigin(origins = "*")
public class PaymentController {
   // Injects PaymentService dependency
   private final PaymentService paymentService;

   /**
    * Create a new payment for an order.
    * 
    * @param paymentRequest request payload containing payment details
    * @return id of created payment
    */
   @PostMapping
   public ResponseEntity<Integer> createPayment(
         @RequestBody @Valid PaymentRequest paymentRequest) {
      log.info("Received payment request: {}", paymentRequest);
      Integer paymentId = paymentService.createPayment(paymentRequest);
      log.info("Payment created with id: {}", paymentId);
      return ResponseEntity.ok(paymentId);
   }
}
