package com.example.demo.entity;
import lombok.Data;

@Data
public class CartRequest {
    private String username;
    private Long productId;
    private int quantity = 1;
}