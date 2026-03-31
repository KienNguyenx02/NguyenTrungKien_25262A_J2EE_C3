package com.example.productapp.repository;

import com.example.productapp.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
    Page<Product> findByCategoryIdAndNameContainingIgnoreCase(Long categoryId, String keyword, Pageable pageable);
}
