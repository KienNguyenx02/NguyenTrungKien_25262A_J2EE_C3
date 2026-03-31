package com.example.productapp.service.impl;

import com.example.productapp.entity.Product;
import com.example.productapp.repository.ProductRepository;
import com.example.productapp.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository repository;

    public ProductServiceImpl(ProductRepository repository) {
        this.repository = repository;
    }

    @Override public Page<Product> getAll(Pageable pageable) { return repository.findAll(pageable); }
    @Override public Page<Product> search(String keyword, Pageable pageable) { return repository.findByNameContainingIgnoreCase(keyword, pageable); }
    @Override public Page<Product> searchAndFilter(Long categoryId, String keyword, Pageable pageable) {
        if (categoryId != null) {
            return repository.findByCategoryIdAndNameContainingIgnoreCase(categoryId, keyword != null ? keyword : "", pageable);
        }
        return repository.findByNameContainingIgnoreCase(keyword != null ? keyword : "", pageable);
    }
    @Override public Product getById(Long id) { return repository.findById(id).orElse(null); }
    @Override public void save(Product product) { repository.save(product); }
    @Override public void delete(Long id) { repository.deleteById(id); }
}
