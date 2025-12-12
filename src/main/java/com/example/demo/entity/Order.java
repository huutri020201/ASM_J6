package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Orders")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderCode;
    private Double totalAmount;
    private String receiverName;
    private String phoneNumber;
    private String shippingAddress;
    private LocalDateTime orderDate;
    private String status;

    @ManyToOne
    @JoinColumn(name = "username")
    private User user;
}



