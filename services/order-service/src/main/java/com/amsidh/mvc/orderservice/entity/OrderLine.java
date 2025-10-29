package com.amsidh.mvc.orderservice.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * OrderLine entity representing individual items in an order.
 * Each order line contains product ID and quantity for a specific product in an
 * order.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class OrderLine {
    @Id
    @GeneratedValue
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private Integer productId;
    private double quantity;

}
