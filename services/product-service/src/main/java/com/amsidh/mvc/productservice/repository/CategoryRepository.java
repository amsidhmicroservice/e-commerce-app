package com.amsidh.mvc.productservice.repository;

import com.amsidh.mvc.productservice.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
