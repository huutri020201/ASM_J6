package com.example.demo.controller;

import com.example.demo.entity.Cart;
import com.example.demo.entity.CartItem;
import com.example.demo.entity.User;
import com.example.demo.service.CartService;
import com.example.demo.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserRepo userRepository; // để lấy User từ username

    @GetMapping("/{username}")
    public ResponseEntity<Cart> getCart(@PathVariable String username) {
        if (username == null || username.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // Tìm user
        User user = userRepository.findById(username).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        // Tìm cart theo user
        return cartService.getCartByUser(user)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping("/{username}/add")
    public ResponseEntity<CartItem> addToCart(@PathVariable String username,
                                              @RequestParam Long productId,
                                              @RequestParam Integer quantity) {
        User user = userRepository.findById(username).orElseThrow();
        Cart cart = cartService.getCartByUser(user).orElse(new Cart());
        cart.setUser(user);
        CartItem item = cartService.addItem(cart, productId, quantity);
        return ResponseEntity.ok(item);
    }

    // Xóa sản phẩm
    @DeleteMapping("/item/{itemId}")
    public ResponseEntity<Void> removeItem(@PathVariable Long itemId) {
        cartService.removeItem(itemId);
        return ResponseEntity.noContent().build();
    }

    // Cập nhật số lượng
    @PutMapping("/item/{itemId}")
    public ResponseEntity<CartItem> updateItem(@PathVariable Long itemId,
                                               @RequestParam Integer quantity) {
        CartItem item = cartService.updateQuantity(itemId, quantity);
        return ResponseEntity.ok(item);
    }
}
