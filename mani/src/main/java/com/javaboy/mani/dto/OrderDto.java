package com.javaboy.mani.dto;

import com.javaboy.mani.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private Long id;
    private Long userId;
    private String userName;
    private BigDecimal totalAmount;
    private LocalDate orderDate;
    private OrderStatus orderStatus;
    private List<OrderItemDto> orderItems;
    private String shippingAddress;
    private String trackingNumber;
    private int totalItems;
}
