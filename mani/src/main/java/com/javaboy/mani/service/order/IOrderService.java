package com.javaboy.mani.service.order;

import com.javaboy.mani.model.Order;

import java.util.List;

public interface IOrderService {
    Order placeOrder(long userId);
    List<Order> getUserOrders(long userId);
}
