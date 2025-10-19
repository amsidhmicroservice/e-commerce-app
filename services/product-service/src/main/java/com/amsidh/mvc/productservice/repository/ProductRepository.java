package com.amsidh.mvc.productservice.repository;

import com.amsidh.mvc.productservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
