package com.example.productapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tên sản phẩm không được để trống")
    private String name;

    @Min(value = 1, message = "Giá tối thiểu là 1")
    @Max(value = 999999, message = "Giá tối đa là 999999")
    private double price;

    @Size(max = 200, message = "Tên hình ảnh không quá 200 ký tự")
    private String image;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    // No-args constructor
    public Product() {}

    // All-args constructor
    public Product(Long id, String name, double price, String image, Category category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.category = category;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
}
