package com.javaboy.mani.repository;

import com.javaboy.mani.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface orderItemRepository  extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByProductId(Long productId);

}
