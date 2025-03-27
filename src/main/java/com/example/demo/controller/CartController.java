package com.example.demo.controller;

import com.example.demo.entity.Cart;
import com.example.demo.entity.CartItem;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {
    private final Cart cart = new Cart();

    @PostMapping("/add")
    public void addItem(@RequestBody CartItem item) {
        cart.addItem(item);
    }

    @DeleteMapping("/remove/{productId}")
    public void removeItem(@PathVariable String productId) {
        cart.removeItem(productId);
    }

    @GetMapping("/total")
    public double getTotalPrice() {
        return cart.getTotalPrice();
    }

    @GetMapping("/items")
    public List<CartItem> getItems() {
        return cart.getItems();
    }
}