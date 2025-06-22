package com.javaboy.mani.service.order;

import com.javaboy.mani.enums.OrderStatus;
import com.javaboy.mani.model.Cart;
import com.javaboy.mani.model.Order;
import com.javaboy.mani.model.OrderItem;
import com.javaboy.mani.model.Product;
import com.javaboy.mani.repository.OrderRepository;
import com.javaboy.mani.repository.ProductRepository;
import com.javaboy.mani.service.cart.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{
    private final OrderRepository orderRepo;
    private final ProductRepository productRepo;
    private final ICartService cartService;
    
    @Override
    public Order placeOrder(long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        Order order = createOrder(cart);
        List<OrderItem> orderItemList = createOrderItems(order, cart);
        order.setOrderItems(new HashSet<>(orderItemList));
        order.setTotalAmount(calculateTotalAmount(orderItemList));
        Order savedorder = orderRepo.save(order);
        cartService.clearCart(cart.getId());
        return savedorder;
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
                .map(item -> item.getPrice()
                        .multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public List<Order> getUserOrders(long userId) {
        return orderRepo.findByUserId(userId);
    }
}
