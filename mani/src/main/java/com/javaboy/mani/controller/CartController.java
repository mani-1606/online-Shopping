package com.javaboy.mani.controller;

import com.javaboy.mani.model.Cart;
import com.javaboy.mani.response.ApiResponse;
import com.javaboy.mani.service.cart.ICartService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/carts")
public class CartController {
    private final ICartService cartService;
    @GetMapping("/user/{userId}/cart")
    public ResponseEntity<ApiResponse> getUserCart(@PathVariable Long userId){
        try {
            Cart cart = cartService.getCartByUserId(userId);
            return ResponseEntity.ok(new ApiResponse("Cart retrieved successfully", cart));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse("Failed to retrieve cart: " + e.getMessage(), null));
        }
    }

    public void clearCart(@PathVariable Long userId){

        try {
            cartService.clearCart(userId);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Cart not found for user ID: " + userId);
        }

    }
}

