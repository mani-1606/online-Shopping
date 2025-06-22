package com.javaboy.mani.mapper;

import com.javaboy.mani.dto.OrderDto;
import com.javaboy.mani.dto.UserWithOrdersDto;
import com.javaboy.mani.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserOrderMapper {

    public static UserWithOrdersDto toUserWithOrdersDto(User user, List<OrderDto> orders) {
        if (user == null) return null;

        UserWithOrdersDto dto = new UserWithOrdersDto();
        dto.setId(user.getId());
        dto.setFirstname(user.getFirstname());
        dto.setLastname(user.getLastname());
        dto.setEmail(user.getEmail());

        // Set full name
        String firstName = user.getFirstname() != null ? user.getFirstname() : "";
        String lastName = user.getLastname() != null ? user.getLastname() : "";
        dto.setName(firstName + " " + lastName);

        // Set orders
        dto.setOrders(orders != null ? orders : new ArrayList<>());
        dto.setTotalOrders(orders != null ? orders.size() : 0);

        return dto;
    }
}
