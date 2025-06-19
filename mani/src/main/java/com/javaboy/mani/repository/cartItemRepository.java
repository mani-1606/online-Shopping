package com.javaboy.mani.repository;

import com.javaboy.mani.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface cartItemRepository extends JpaRepository<CartItem, Long> {
    static List<CartItem> findByProductId(Long productId);
}
