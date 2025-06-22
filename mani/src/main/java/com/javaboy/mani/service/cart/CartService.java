package com.javaboy.mani.service.cart;

import com.javaboy.mani.model.Cart;
import com.javaboy.mani.model.User;
import com.javaboy.mani.repository.CartRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService{
    private final CartRepository cartRepo;
    @Override
    public Cart getCart(long cartId) {
        Cart cart = cartRepo.findById(cartId).orElseThrow(()
                -> new EntityNotFoundException("Cart not found with ID: " + cartId));
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        return cartRepo.save(cart);
    }

    @Override
    public Cart getCartByUserId(long userId) {
        return cartRepo.findByUserId(userId);
    }


    @Override
    public void clearCart(long cartId) {
        Cart cart = getCart(cartId);
        cartRepo.deleteAllByCartId(cartId);
        cart.clearCart();
        cartRepo.deleteById(cartId);
    }

    @Override
    public Cart initializeNewCartForUser(User user) {
        return Optional.ofNullable(getCartByUserId(user.getId())).orElseGet(() ->{
            Cart cart = new Cart();
            cart.setUser(user);
            return cartRepo.save(cart);
        });
    }

    @Override
    public BigDecimal getTotalPrice(long cartId) {
        Cart cart = getCart(cartId);
        return cart.getTotalAmount();

    }
}
