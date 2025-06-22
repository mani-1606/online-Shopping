package com.javaboy.mani.controller;

import com.javaboy.mani.dto.OrderDto;
import com.javaboy.mani.dto.UserWithOrdersDto;
import com.javaboy.mani.response.ApiResponse;
import com.javaboy.mani.service.order.IOrderService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/orders")
public class OrderController {
    private final IOrderService orderService;
    @PostMapping("/user/order")
    public ResponseEntity<ApiResponse> placeOrder(@RequestParam Long userId){
        try {
            OrderDto order = orderService.placeOrder(userId);
            return ResponseEntity.ok(new ApiResponse("Order placed successfully", order));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(new ApiResponse("User not found", e.getMessage()));
        }
    }
    @GetMapping("/user/{userId}/order")
    public ResponseEntity<ApiResponse> getUserOrders(@PathVariable Long userId){
        try {
            List<OrderDto> orders = orderService.getUserOrders(userId);
            return ResponseEntity.ok(new ApiResponse("Orders retrieved successfully", orders));
        } catch( EntityNotFoundException e) {
            return ResponseEntity.status(404).body(new ApiResponse("User not found", e.getMessage()));
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long orderId) {
        try {
            OrderDto orderDto = orderService.getOrderById(orderId);
            return ResponseEntity.ok(new ApiResponse("Order retrieved successfully", orderDto));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(new ApiResponse("Order not found", e.getMessage()));
        }
    }

    @GetMapping("/user/{userId}/details")
    public ResponseEntity<ApiResponse> getUserWithOrders(@PathVariable Long userId) {
        try {
            UserWithOrdersDto userWithOrders = orderService.getUserWithOrders(userId);
            return ResponseEntity.ok(new ApiResponse("User with orders retrieved successfully", userWithOrders));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(new ApiResponse("User not found", e.getMessage()));
        }
    }
}
