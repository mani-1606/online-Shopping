package com.javaboy.mani.service.order;

import com.javaboy.mani.dto.OrderDto;
import com.javaboy.mani.dto.UserWithOrdersDto;
import com.javaboy.mani.model.Order;

import java.util.List;

public interface IOrderService {
    OrderDto placeOrder(long userId);
    List<OrderDto> getUserOrders(long userId);
    OrderDto getOrderById(long orderId);
    UserWithOrdersDto getUserWithOrders(long userId);
}
