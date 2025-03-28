// Cart.java
package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    Account customer;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    List<CartItem> cartItems = new ArrayList<>();

    @ManyToMany(mappedBy = "carts")
    @JsonIgnore
    List<Product> products;

    public void addItem(CartItem item) {
        cartItems.add(item);
    }

    public void removeItem(String productId) {
        cartItems.removeIf(item -> productId.equals(item.getProductId()));
    }

    public double getTotalPrice() {
        return cartItems.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
    }

    public List<CartItem> getItems() {
        return cartItems;
    }
}