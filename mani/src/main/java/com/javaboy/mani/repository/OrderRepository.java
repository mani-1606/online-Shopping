package com.javaboy.mani.repository;

import com.javaboy.mani.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(long userId);

}
