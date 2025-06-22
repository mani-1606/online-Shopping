package com.javaboy.mani.service.order;

import com.javaboy.mani.dto.OrderDto;
import com.javaboy.mani.dto.UserDto;
import com.javaboy.mani.dto.UserWithOrdersDto;
import com.javaboy.mani.enums.OrderStatus;
import com.javaboy.mani.mapper.OrderMapper;
import com.javaboy.mani.mapper.UserMapper;
import com.javaboy.mani.mapper.UserOrderMapper;
import com.javaboy.mani.model.Cart;
import com.javaboy.mani.model.Order;
import com.javaboy.mani.model.OrderItem;
import com.javaboy.mani.model.Product;
import com.javaboy.mani.model.User;
import com.javaboy.mani.repository.OrderRepository;
import com.javaboy.mani.repository.ProductRepository;
import com.javaboy.mani.service.cart.ICartService;
import com.javaboy.mani.service.user.IUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{
    private final OrderRepository orderRepo;
    private final ProductRepository productRepo;
    private final ICartService cartService;
    private final IUserService userService;
    
    @Override
    public OrderDto placeOrder(long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        Order order = createOrder(cart);
        List<OrderItem> orderItemList = createOrderItems(order, cart);
        order.setOrderItems(new HashSet<>(orderItemList));
        order.setTotalAmount(calculateTotalAmount(orderItemList));
        Order savedOrder = orderRepo.save(order);
        cartService.clearCart(cart.getId());
        return OrderMapper.toDto(savedOrder);
    }
    
    private Order createOrder(Cart cart){
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return order;
    }
    
    private List<OrderItem> createOrderItems(Order order, Cart cart){
        return cart.getItems().stream().map(cartItem ->{
            Product product = cartItem.getProduct();
            product.setInventory(product.getInventory() - cartItem.getQuantity());
            productRepo.save(product);
            return new OrderItem(
                    order,
                    product,
                   cartItem.getUnitPrice(),
                    cartItem.getQuantity());
        }).toList();
    }
    
    private BigDecimal calculateTotalAmount(List<OrderItem> orderItemList){
        return orderItemList.stream()
                .map(item -> {
                    BigDecimal price = item.getPrice();
                    if (price == null) {
                        price = BigDecimal.ZERO;
                    }
                    return price.multiply(new BigDecimal(item.getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public List<OrderDto> getUserOrders(long userId) {
        List<Order> orders = orderRepo.findByUserId(userId);
        return orders.stream()
                .map(OrderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDto getOrderById(long orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + orderId));
        return OrderMapper.toDto(order);
    }

    @Override
    public UserWithOrdersDto getUserWithOrders(long userId) {
        // Get user
        UserDto userDto = userService.getUserById(userId);
        if (userDto == null) {
            throw new EntityNotFoundException("User not found with ID: " + userId);
        }

        // Get user's orders
        List<OrderDto> orders = getUserOrders(userId);

        // Convert to UserWithOrdersDto
        User user = UserMapper.toEntity(userDto);
        return UserOrderMapper.toUserWithOrdersDto(user, orders);
    }
}
