package com.example.demo.service;

import com.example.demo.dto.OrderDetailDto;
import com.example.demo.dto.OrderRequest;
import com.example.demo.dto.OrderSummaryDto;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    OrderDetailRepo orderDetailRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    CartRepo cartRepo;

    @Autowired
    CartItemRepo cartItemRepo;
    @Transactional
    public Order createOrder(OrderRequest request) {

        User user = userRepo.findById(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        Cart cart = cartRepo.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Giỏ hàng trống"));

        // 1. Tạo ORDER
        Order order = new Order();
        order.setOrderCode("ORD-" + UUID.randomUUID().toString().substring(0, 8));
        order.setTotalAmount(request.getTotalAmount());
        order.setReceiverName(request.getReceiverName());
        order.setPhoneNumber(request.getPhoneNumber());
        order.setShippingAddress(request.getShippingAddress());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");
        order.setUser(user);

        order = orderRepo.save(order);

        // 2. Lưu ORDER DETAILS từ CART ITEM
        for (CartItem item : cart.getItems()) {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setProduct(item.getProduct());
            detail.setQuantity(item.getQuantity());
            detail.setPrice(item.getPrice());

            orderDetailRepo.save(detail);
        }

        // 3. XÓA GIỎ HÀNG SAU KHI ĐẶT
        cartItemRepo.deleteByCartId(cart.getId());

        return order;
    }
    public List<OrderSummaryDto> getOrdersByUsername(String username) {
        List<Order> orders = orderRepo.findByUserUsernameOrderByOrderDateDesc(username);
        return orders.stream().map(o -> {
            OrderSummaryDto dto = new OrderSummaryDto();
            dto.setId(o.getId());
            dto.setOrderCode(o.getOrderCode());
            dto.setTotalAmount(o.getTotalAmount());
            dto.setOrderDate(o.getOrderDate());
            dto.setStatus(o.getStatus());
            return dto;
        }).collect(Collectors.toList());
    }

    public List<OrderDetailDto> getOrderDetails(Long orderId) {
        List<OrderDetail> details = orderDetailRepo.findByOrderId(orderId);
        return details.stream().map(d -> {
            OrderDetailDto dto = new OrderDetailDto();
            dto.setId(d.getId());
            dto.setProductId(d.getProduct().getId());
            dto.setProductName(d.getProduct().getName());
            dto.setProductImage(d.getProduct().getImageUrl());
            dto.setQuantity(d.getQuantity());
            dto.setPrice(d.getPrice());
            return dto;
        }).collect(Collectors.toList());
    }
    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }
    public Order updateStatus(Long id, String status) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));

        order.setStatus(status);
        return orderRepo.save(order);
    }
}
