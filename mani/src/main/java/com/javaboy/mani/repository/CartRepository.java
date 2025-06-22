package com.javaboy.mani.repository;

import com.javaboy.mani.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByUserId(long userId);

    void deleteAllByCartId(long cartId);

}
