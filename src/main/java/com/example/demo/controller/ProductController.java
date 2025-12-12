package com.example.demo.controller;

import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
public class ProductController {
    @Autowired
    ProductRepo productRepo;
    @GetMapping("admin/product")
    public List<Product> findAll() {
        return productRepo.findAll();
    }
    @GetMapping("admin/product/{id}")
    public Product findById(@PathVariable("id") Long id) {
        return productRepo.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với id: " + id));
    }
    @PostMapping("admin/product")
    public Product save(@RequestBody Product product) {
        return productRepo.save(product);
    }
    @PutMapping("admin/product/{id}")
    public Product update(@PathVariable("id") Long id, @RequestBody Product product) {
        return productRepo.save(product);
    }
    @DeleteMapping("admin/product/{id}")
    public void delete(@PathVariable("id") Long id) {
        productRepo.deleteById(id);
    }
}
