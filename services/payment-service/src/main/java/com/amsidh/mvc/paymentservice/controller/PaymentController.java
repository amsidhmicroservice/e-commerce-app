package com.amsidh.mvc.paymentservice.controller;

import com.amsidh.mvc.paymentservice.dto.PaymentRequest;
import com.amsidh.mvc.paymentservice.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/payments")
@Slf4j
@CrossOrigin(origins = "*")
public class PaymentController {
   private final PaymentService paymentService;

   @PostMapping
   public ResponseEntity<Integer> createPayment(
           @RequestBody @Valid PaymentRequest paymentRequest) {
      log.info("Received payment request: {}", paymentRequest);
      Integer paymentId = paymentService.createPayment(paymentRequest);
      log.info("Payment created with id: {}", paymentId);
      return ResponseEntity.ok(paymentId);
   }
}
