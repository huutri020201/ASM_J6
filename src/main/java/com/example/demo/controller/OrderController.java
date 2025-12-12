package com.example.demo.controller;

import com.example.demo.dto.OrderDetailDto;
import com.example.demo.dto.OrderRequest;
import com.example.demo.dto.OrderSummaryDto;
import com.example.demo.entity.Order;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping("/api/order/create")
    public Order create(@RequestBody OrderRequest request) {
        return orderService.createOrder(request);
    }

    // Lấy danh sách đơn hàng theo username
    @GetMapping("/api/order/user/{username}")
    public List<OrderSummaryDto> getOrdersByUser(@PathVariable String username) {
        return orderService.getOrdersByUsername(username);
    }

    // Lấy chi tiết order theo orderId
    @GetMapping("/api/order/{orderId}/details")
    public List<OrderDetailDto> getOrderDetails(@PathVariable Long orderId) {
        return orderService.getOrderDetails(orderId);
    }

    // Lấy toàn bộ đơn hàng
    @GetMapping("/admin/orders")
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    // Cập nhật trạng thái
    @PutMapping("/admin/orders/{id}/status")
    public Order updateStatus(
            @PathVariable Long id,
            @RequestParam String status
    ) {
        return orderService.updateStatus(id, status);
    }
}

