package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OrderSummaryDto {
    private Long id;
    private String orderCode;
    private Double totalAmount;
    private LocalDateTime orderDate;
    private String status;
}
