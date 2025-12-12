package com.example.demo.controller;

import com.example.demo.entity.Category;
import com.example.demo.repository.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
public class CategoryController {
    @Autowired
    CategoryRepo categoryRepo;
    @GetMapping("category")
    public List<Category> findAll() {
        return categoryRepo.findAll();
    }
    @GetMapping("category/{id}")
    public Category findById(@PathVariable("id") Long id) {
        return categoryRepo.findById(id).get();
    }
}
