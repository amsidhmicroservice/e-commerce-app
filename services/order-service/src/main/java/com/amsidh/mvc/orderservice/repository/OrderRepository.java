package com.amsidh.mvc.orderservice.repository;

import com.amsidh.mvc.orderservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
