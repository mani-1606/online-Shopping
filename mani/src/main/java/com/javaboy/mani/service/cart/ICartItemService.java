package com.javaboy.mani.service.cart;

import com.javaboy.mani.model.CartItem;

public interface ICartItemService {
    void addItemToCart(long cartId, long productId, int quantity);
    void removeItemFromCart(long cartId, long productId);
    void updateItemQuantity(long cartId, long productId, int quantity);
    CartItem getCartItem(long cartId, long productId);
}
