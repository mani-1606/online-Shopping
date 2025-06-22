package com.javaboy.mani.controller;

import com.javaboy.mani.dto.UserDto;
import com.javaboy.mani.model.Cart;
import com.javaboy.mani.model.User;
import com.javaboy.mani.response.ApiResponse;
import com.javaboy.mani.service.cart.ICartItemService;
import com.javaboy.mani.service.cart.ICartService;
import com.javaboy.mani.service.user.IUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/cartitems")
public class CartItemController {
    private final ICartItemService cartItemService;
    private final IUserService  userService;
    private  final ICartService cartService;
    @PostMapping("/item/add")
    public ResponseEntity<ApiResponse> addItemToCart(
                                                            Long userId, 
                                              @RequestParam Long productId, 
                                              @RequestParam Integer quantity) {
        try {
            UserDto userDto = userService.getUserById(userId);
            User user = com.javaboy.mani.mapper.UserMapper.toEntity(userDto);
            Cart cart = cartService.initializeNewCartForUser(user);
            cartItemService.addItemToCart(cart.getId(), productId, quantity);
            return ResponseEntity.ok( new ApiResponse("Item added to cart successfully", null));
        } catch (  ExceptionInInitializerError e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse("Failed to add item to cart: " + e.getMessage(), null));
        }

    }
    @DeleteMapping("/cart/{cartId}/item/{itemId}/remove")
    public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable Long cartId,
                                                          @PathVariable Long itemId){
        try {
            cartItemService.removeItemFromCart(cartId, itemId);
            return ResponseEntity.ok(new ApiResponse("Item removed from cart successfully", null));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse("Failed to remove item from cart: " + e.getMessage(), null));
        }
    }
    @PutMapping("/cart/{cartId}/item/{itemId}/update")
    public ResponseEntity<ApiResponse> updateCartItem(
            @PathVariable Long cartId,
            @PathVariable Long itemId,
            @RequestParam Integer quantity) {
        try {
            cartItemService.updateItemQuantity(cartId, itemId, quantity);
            return ResponseEntity.ok(new ApiResponse("Cart item updated successfully", null));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse("Failed to update cart item: " + e.getMessage(), null));
        }
    }


}
