package com.javaboy.mani.mapper;

import com.javaboy.mani.dto.OrderDto;
import com.javaboy.mani.dto.OrderItemDto;
import com.javaboy.mani.model.Order;
import com.javaboy.mani.model.OrderItem;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    public static OrderDto toDto(Order order) {
        if (order == null) return null;

        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setUserId(order.getUser() != null ? order.getUser().getId() : null);
        orderDto.setUserName(order.getUser() != null ? order.getUser().getName() : null);
        orderDto.setTotalAmount(order.getTotalAmount());
        orderDto.setOrderDate(order.getOrderDate());
        orderDto.setOrderStatus(order.getOrderStatus());
        orderDto.setShippingAddress(order.getShippingAddress());
        orderDto.setTrackingNumber(order.getTrackingNumber());

        // Convert order items
        if (order.getOrderItems() != null) {
            List<OrderItemDto> orderItemDtos = order.getOrderItems().stream()
                    .map(OrderMapper::toItemDto)
                    .collect(Collectors.toList());
            orderDto.setOrderItems(orderItemDtos);
            orderDto.setTotalItems(orderItemDtos.size());
        } else {
            orderDto.setOrderItems(java.util.Collections.emptyList());
            orderDto.setTotalItems(0);
        }

        return orderDto;
    }

    public static OrderItemDto toItemDto(OrderItem orderItem) {
        if (orderItem == null) return null;

        OrderItemDto dto = new OrderItemDto();
        dto.setId(orderItem.getId());
        dto.setQuantity(orderItem.getQuantity());
        dto.setUnitPrice(orderItem.getPrice());

        // Calculate total price
        if (orderItem.getPrice() != null) {
            dto.setTotalPrice(orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity())));
        } else {
            dto.setTotalPrice(BigDecimal.ZERO);
        }

        // Set product details if available
        if (orderItem.getProduct() != null) {
            dto.setProductId(orderItem.getProduct().getId());
            dto.setProductName(orderItem.getProduct().getName());

            // Get the first image URL if available
            if (orderItem.getProduct().getImages() != null && !orderItem.getProduct().getImages().isEmpty()) {
                dto.setProductImage(orderItem.getProduct().getImages().get(0).getUrl());
            }
        }

        return dto;
    }

    // If needed, add a method to convert from DTO to entity
    public static Order toEntity(OrderDto orderDto) {
        // Implementation for converting from DTO to entity if needed
        return null; // Placeholder
    }
}
