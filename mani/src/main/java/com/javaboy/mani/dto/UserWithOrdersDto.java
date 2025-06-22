package com.javaboy.mani.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserWithOrdersDto {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String name; // Combined first and last name
    private List<OrderDto> orders;
    private int totalOrders;
}
