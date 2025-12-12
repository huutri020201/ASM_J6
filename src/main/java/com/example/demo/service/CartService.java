package com.example.demo.service;

import com.example.demo.entity.Cart;
import com.example.demo.entity.CartItem;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.repository.CartItemRepo;
import com.example.demo.repository.CartRepo;
import com.example.demo.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private CartItemRepo itemRepo;

    @Autowired
    private ProductRepo productRepo;

    // Lấy cart theo user
    public Optional<Cart> getCartByUser(User user) {
        return cartRepo.findByUser(user);
    }

    // Thêm sản phẩm
    public CartItem addItem(Cart cart, Long productId, Integer quantity) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Nếu đã có trong cart, tăng số lượng
        Optional<CartItem> existing = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst();

        if (existing.isPresent()) {
            CartItem item = existing.get();
            item.setQuantity(item.getQuantity() + quantity);
            return itemRepo.save(item);
        }

        CartItem newItem = new CartItem();
        newItem.setCart(cart);
        newItem.setProduct(product);
        newItem.setQuantity(quantity);
        newItem.setPrice(product.getPrice());
        cart.getItems().add(newItem);
        cartRepo.save(cart); // Cascade sẽ save CartItem
        return newItem;
    }

    // Xóa sản phẩm
    public void removeItem(Long itemId) {
        itemRepo.deleteById(itemId);
    }

    // Cập nhật số lượng
    public CartItem updateQuantity(Long itemId, Integer quantity) {
        CartItem item = itemRepo.findById(itemId)
                .orElseThrow(() -> new RuntimeException("CartItem not found"));
        item.setQuantity(quantity);
        return itemRepo.save(item);
    }
}

