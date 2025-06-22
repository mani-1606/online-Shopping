package com.javaboy.mani.model;


import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Cart")

public class Cart {
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;
     private BigDecimal totalAmount;
     @OneToOne
     @JoinColumn(name = "user_id")
     private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL,orphanRemoval = true)
     private Set<CartItem> cartItems = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Cart(Long id, BigDecimal totalAmount, User user) {
        this.id = id;
        this.totalAmount = totalAmount;
        this.user = user;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", totalAmount=" + totalAmount +
                ", user=" + user +
                '}';
    }

    public Cart() {
    }


    public void removeItem(CartItem cartItem) {
        this.cartItems.remove(cartItem);
        cartItem.setCart(null);
        updateTotalAmount();
    }

    private void updateTotalAmount() {
        this.totalAmount = cartItems.stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    public Set<CartItem> getItems() {
        return cartItems;
    }

    public void addItem(CartItem cartItem) {
        if (cartItem != null) {
            cartItem.setCart(this);
            this.cartItems.add(cartItem);
            updateTotalAmount();
        }
    }

    public void clearCart() {
        for (CartItem item : cartItems) {
            item.setCart(null);
        }
        this.cartItems.clear();
        this.totalAmount = BigDecimal.ZERO;
    }
}
