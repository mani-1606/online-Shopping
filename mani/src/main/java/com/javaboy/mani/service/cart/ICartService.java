package com.javaboy.mani.service.cart;

import com.javaboy.mani.model.Cart;
import com.javaboy.mani.model.User;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(long cartId);

    Cart getCartByUserId(long userId);

    void clearCart(long cartId);

    Cart initializeNewCartForUser(User user);

    BigDecimal getTotalPrice(long cartId);
}
