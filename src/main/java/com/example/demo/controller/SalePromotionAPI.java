// src/main/java/com/example/demo/controller/SalePromotionAPI.java
package com.example.demo.controller;

import com.example.demo.entity.SalePromotion;
import com.example.demo.service.SalePromotionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sale-promotion")
@SecurityRequirement(name = "api")
@CrossOrigin(origins = "*")
public class SalePromotionAPI {

    @Autowired
    SalePromotionService salePromotionService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('STAFF')")

    public ResponseEntity<SalePromotion> create(@RequestBody SalePromotion salePromotion) {
        SalePromotion newSalePromotion = salePromotionService.create(salePromotion);
        return ResponseEntity.ok(newSalePromotion);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalePromotion> getById(@PathVariable Long id) {
        SalePromotion salePromotion = salePromotionService.getById(id);
        return ResponseEntity.ok(salePromotion);
    }

    @GetMapping
    public ResponseEntity<List<SalePromotion>> getAll() {
        List<SalePromotion> salePromotions = salePromotionService.getAll();
        return ResponseEntity.ok(salePromotions);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")

    public ResponseEntity<SalePromotion> update(@PathVariable Long id, @RequestBody SalePromotion salePromotion) {
        SalePromotion updatedSalePromotion = salePromotionService.update(id, salePromotion);
        return ResponseEntity.ok(updatedSalePromotion);
    }

    public ResponseEntity<Void> delete(@PathVariable Long id) {
        salePromotionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    public ResponseEntity<SalePromotion> approve(@PathVariable Long id) {
        SalePromotion approvedSalePromotion = salePromotionService.approve(id);
        return ResponseEntity.ok(approvedSalePromotion);
    }

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    public ResponseEntity<SalePromotion> reject(@PathVariable Long id) {
        SalePromotion rejectedSalePromotion = salePromotionService.reject(id);
        return ResponseEntity.ok(rejectedSalePromotion);
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    public ResponseEntity<Void> deleteSalePromotion(@PathVariable Long id) {
        salePromotionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}