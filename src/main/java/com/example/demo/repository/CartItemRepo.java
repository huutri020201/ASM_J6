package com.example.demo.repository;

import com.example.demo.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface CartItemRepo extends JpaRepository<CartItem, Long> {
        @Modifying
        @Query("DELETE FROM CartItem c WHERE c.cart.id = :cartId")
        void deleteByCartId(@Param("cartId") Long cartId);
}
