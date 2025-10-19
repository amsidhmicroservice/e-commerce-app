package com.amsidh.mvc.paymentservice.repository;

import com.amsidh.mvc.paymentservice.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
}
