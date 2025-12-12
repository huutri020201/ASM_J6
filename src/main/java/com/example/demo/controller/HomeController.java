package com.example.demo.controller;

import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.repository.CategoryRepo;
import com.example.demo.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin("*")
@RestController
public class HomeController {
    @Autowired
    CategoryRepo categoryRepo;
    @Autowired
    ProductRepo productRepo;
    @GetMapping("/products/{slug}")
    public List<Product> getProductsByCategory(@PathVariable String slug) {
        Category category = categoryRepo.findBySlug(slug);
        if (category == null) return List.of();
        return productRepo.findByCategory(category);
    }
    @GetMapping("/products/detail/{productSlug}")
    public Product getProductBySlug(@PathVariable String productSlug) {
        return productRepo.findBySlug(productSlug);
    }
    @GetMapping("/products/latest/{categorySlug}")
    public List<Product> getLatestProductsByCategory(@PathVariable String categorySlug) {
        Category category = categoryRepo.findBySlug(categorySlug);
        if (category == null) return List.of();
        return productRepo.findTop4ByCategoryOrderByIdDesc(category); // top 4 mới nhất
    }

}
