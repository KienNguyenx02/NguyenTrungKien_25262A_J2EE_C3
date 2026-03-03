package com.example.productapp.service.impl;

import com.example.productapp.entity.Product;
import com.example.productapp.repository.ProductRepository;
import com.example.productapp.service.ProductService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository repository;

    public ProductServiceImpl(ProductRepository repository) {
        this.repository = repository;
    }

    @Override public List<Product> getAll() { return repository.findAll(); }
    @Override public Product getById(Long id) { return repository.findById(id).orElse(null); }
    @Override public void save(Product product) { repository.save(product); }
    @Override public void delete(Long id) { repository.deleteById(id); }
}
