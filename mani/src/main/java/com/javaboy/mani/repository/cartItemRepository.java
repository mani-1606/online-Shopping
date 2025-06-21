package com.javaboy.mani.repository;

import com.javaboy.mani.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface cartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByProductId(Long productId);

    List<CartItem> findByCartId(Long cartId);

    CartItem findByCartIdAndProductId(Long cartId, Long productId);

    void deleteByCartId(Long cartId);

    int countByProductId(Long productId);
}
