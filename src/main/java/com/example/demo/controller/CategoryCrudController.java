package com.example.demo.controller;

import com.example.demo.entity.Category;
import com.example.demo.repository.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
public class CategoryCrudController {
    @Autowired
    CategoryRepo categoryRepo;
    @GetMapping("admin/category")
    public List<Category> findAll() {
        return categoryRepo.findAll();
    }
    @GetMapping("admin/category/{id}")
    public Category findById(@PathVariable("id") Long id) {
        return categoryRepo.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với id: " + id));
    }
    @PostMapping("admin/category")
    public Category save(@RequestBody Category category) {
        return categoryRepo.save(category);
    }
    @PutMapping("admin/category/{id}")
    public Category update(@PathVariable("id") Long id, @RequestBody Category category) {
        return categoryRepo.save(category);
    }
    @DeleteMapping("admin/category/{id}")
    public void delete(@PathVariable("id") Long id) {
        categoryRepo.deleteById(id);
    }
}
