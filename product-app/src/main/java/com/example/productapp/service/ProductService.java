package com.example.productapp.service;

import com.example.productapp.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ProductService {
    Page<Product> getAll(Pageable pageable);
    Page<Product> search(String keyword, Pageable pageable);
    Page<Product> searchAndFilter(Long categoryId, String keyword, Pageable pageable);
    Product getById(Long id);
    void save(Product product);
    void delete(Long id);
}
