package com.example.demo.dto;

import lombok.Data;

@Data
public class OrderRequest {
    private String receiverName;
    private String phoneNumber;
    private String shippingAddress;
    private Double totalAmount;
    private String username;
}
