// CartController.java
package com.example.demo.controller;

import com.example.demo.entity.Account;
import com.example.demo.entity.Cart;
import com.example.demo.entity.CartItem;
import com.example.demo.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private AccountRepository accountRepository;

    private final Cart cart = new Cart();

    @PostMapping("/add")
    public void addItem(@RequestParam String customerId, @RequestBody CartItem item) {
        Account customer = accountRepository.findById(Long.parseLong(customerId)).orElse(null);
        if (customer != null) {
            cart.setCustomer(customer);
            cart.addItem(item);
        }
    }

    @DeleteMapping("/remove/{productId}")
    public void removeItem(@RequestParam String customerId, @PathVariable String productId) {
        Account customer = accountRepository.findById(Long.parseLong(customerId)).orElse(null);
        if (customer != null && cart.getCustomer().getId() == customer.getId()) {
            cart.removeItem(productId);
        }
    }

    @GetMapping("/total")
    public double getTotalPrice(@RequestParam String customerId) {
        Account customer = accountRepository.findById(Long.parseLong(customerId)).orElse(null);
        if (customer != null && cart.getCustomer().getId() == customer.getId()) {
            return cart.getTotalPrice();
        }
        return 0;
    }

    @GetMapping("/items")
    public List<CartItem> getItems(@RequestParam String customerId) {
        Account customer = accountRepository.findById(Long.parseLong(customerId)).orElse(null);
        if (customer != null && cart.getCustomer().getId() == customer.getId()) {
            return cart.getItems();
        }
        return List.of();
    }
}