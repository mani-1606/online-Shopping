package com.javaboy.mani.service.cart;

import com.javaboy.mani.model.Cart;
import com.javaboy.mani.model.CartItem;
import com.javaboy.mani.model.Product;
import com.javaboy.mani.repository.CartRepository;
import com.javaboy.mani.repository.cartItemRepository;
import com.javaboy.mani.service.product.ProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService{
    private final cartItemRepository cartItemRepository;
    private final ICartService cartService;
    private final ProductService productService;
    private final CartRepository cartRepository;
    @Override
    public void addItemToCart(long cartId, long productId, int quantity) {
        Cart cart = cartService.getCart(cartId);
        Product product = productService.getProductById(productId);
        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId() == productId)
                .findFirst().orElse(new CartItem());
        if (cartItem.getId() == null) {
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
            cart.getItems().add(cartItem);
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }
        cartItem.setTotalPrice( product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        cart.addItem(cartItem);
        cartItemRepository.save(cartItem);
    }

    @Override
    public void removeItemFromCart(long cartId, long productId) {
       Cart cart = cartService.getCart(cartId);
       CartItem itemToRemove = getCartItem(cartId, productId);
       cart.removeItem(itemToRemove);
       cartRepository.save(cart);
    }

    @Override
    public void updateItemQuantity(long cartId, long productId, int quantity) {
        Cart cart = cartService.getCart(cartId);
        cart.getItems().stream()
                .filter(item -> item.getProduct().getId() == productId)
                .findFirst()
                .ifPresent(item -> {
                   item.setQuantity(quantity);
                   item.setUnitPrice( item.getProduct().getPrice());
                   item.setTotalPrice( item.getUnitPrice().multiply(BigDecimal.valueOf(quantity)));
                });
        BigDecimal totalAmmount = cart.getItems().stream().map(CartItem :: getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalAmount(totalAmmount);
        cartRepository.save(cart);
    }

    @Override
    public CartItem getCartItem(long cartId, long productId) {
        Cart cart = cartService.getCart(cartId);
        return cart.getItems().stream()
                .filter(item -> item.getProduct().getId() == productId)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("CartItem not found for product ID: " + productId));
    }
}
