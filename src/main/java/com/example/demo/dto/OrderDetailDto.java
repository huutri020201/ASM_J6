package com.example.demo.dto;

import lombok.Data;

@Data
public class OrderDetailDto {
    private Long id;
    private Long productId;
    private String productName;
    private String productImage;
    private Integer quantity;
    private Double price;
}
