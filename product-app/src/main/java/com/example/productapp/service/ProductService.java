package com.example.productapp.service;

import com.example.productapp.entity.Product;
import java.util.List;

public interface ProductService {
    List<Product> getAll();
    Product getById(Long id);
    void save(Product product);
    void delete(Long id);
}
