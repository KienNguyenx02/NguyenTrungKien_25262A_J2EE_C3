package com.example.productapp.controller;

import com.example.productapp.entity.Product;
import com.example.productapp.repository.CategoryRepository;
import com.example.productapp.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final CategoryRepository categoryRepository;
    private static final String UPLOAD_DIR = "uploads/";

    public ProductController(ProductService productService, CategoryRepository categoryRepository) {
        this.productService = productService;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public String list(Model model, 
                       @RequestParam(name = "keyword", defaultValue = "") String keyword,
                       @RequestParam(name = "categoryId", required = false) Long categoryId,
                       @RequestParam(name = "sort", defaultValue = "id,asc") String sort,
                       @RequestParam(name = "page", defaultValue = "0") int page) {
        
        int size = 5; 
        String[] sortParams = sort.split(",");
        Sort sortObj = Sort.by(sortParams[1].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortParams[0]);
        
        Pageable pageable = PageRequest.of(page, size, sortObj);
        Page<Product> productPage = productService.searchAndFilter(categoryId, keyword, pageable);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("sort", sort);
        model.addAttribute("categories", categoryRepository.findAll());
        
        return "product/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryRepository.findAll());
        return "product/create";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Product product, BindingResult result,
                       @RequestParam("imageFile") MultipartFile imageFile, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryRepository.findAll());
            return "product/create";
        }

        if (!imageFile.isEmpty()) {
            try {
                String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(imageFile.getInputStream(), filePath);
                product.setImage(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        productService.save(product);
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.getById(id));
        model.addAttribute("categories", categoryRepository.findAll());
        return "product/edit";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute Product product,
                         BindingResult result, @RequestParam("imageFile") MultipartFile imageFile, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryRepository.findAll());
            return "product/edit";
        }

        Product existingProduct = productService.getById(id);
        if (!imageFile.isEmpty()) {
            try {
                String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(imageFile.getInputStream(), filePath);
                product.setImage(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (existingProduct != null) {
            product.setImage(existingProduct.getImage());
        }
        product.setId(id);
        productService.save(product);
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        productService.delete(id);
        return "redirect:/products";
    }
}
