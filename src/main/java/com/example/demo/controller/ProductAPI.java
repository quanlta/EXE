package com.example.demo.controller;

import com.example.demo.entity.Product;
import com.example.demo.service.ProductService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@SecurityRequirement(name = "api")
@CrossOrigin(origins = "*")
public class ProductAPI {

    @Autowired
    ProductService productService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('STAFF') or hasAuthority('OWNER')")
    public ResponseEntity<Product> create(@RequestBody Product product, Authentication authentication) {
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();
        Product newProduct = productService.create(product, userRole);
        return ResponseEntity.ok(newProduct);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Product>> getAll() {
        List<Product> products = productService.getAll();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
//    public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody Product product, Authentication authentication) {
//        String userRole = authentication.getAuthorities().iterator().next().getAuthority();

    public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody Product product) {
        Product updatedProduct = productService.update(id, product);
        return ResponseEntity.ok(updatedProduct);
    }




    @PutMapping("/changeStatus/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    public ResponseEntity<Product> changeStatus(@PathVariable Long id, @RequestParam String action) {
        Product product = productService.changeStatus(id, action);
        return ResponseEntity.ok(product);
    }
}
