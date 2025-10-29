package com.amsidh.mvc.productservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * Category entity representing a product category.
 * Products are organized into categories for better organization and filtering.
 * Cascade delete ensures all products in a category are removed when the
 * category is deleted.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_seq")
    @SequenceGenerator(name = "category_seq", sequenceName = "category_id_seq", allocationSize = 1)
    private Integer id;
    private String name;
    private String description;
    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE)
    private List<Product> products;
}
