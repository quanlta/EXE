// src/main/java/com/example/demo/service/SalePromotionService.java
package com.example.demo.service;

import com.example.demo.entity.SalePromotion;
import com.example.demo.repository.SalePromotionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalePromotionService {

    @Autowired
    SalePromotionRepository salePromotionRepository;

    public SalePromotion create(SalePromotion salePromotion) {
        salePromotion.setStatus(true);
        salePromotion.setPending(false);
        return salePromotionRepository.save(salePromotion);
    }

    public SalePromotion getById(Long id) {
        return salePromotionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("SalePromotion not found"));
    }

    public List<SalePromotion> getAll() {
        return salePromotionRepository.findAll();
    }

    public SalePromotion update(Long id, SalePromotion salePromotion) {
        SalePromotion existingSalePromotion = getById(id);
        existingSalePromotion.setName(salePromotion.getName());
        existingSalePromotion.setDiscountPercentage(salePromotion.getDiscountPercentage());
        existingSalePromotion.setDiscountAmount(salePromotion.getDiscountAmount());
        existingSalePromotion.setCreatedAt(salePromotion.getCreatedAt());
        existingSalePromotion.setEndAt(salePromotion.getEndAt());
        existingSalePromotion.setProducts(salePromotion.getProducts());
        existingSalePromotion.setPending(true);
        return salePromotionRepository.save(existingSalePromotion);
    }

    public void delete(Long id) {
        SalePromotion salePromotion = getById(id);
        salePromotionRepository.delete(salePromotion);
//        salePromotion.setPending(true);
//        salePromotionRepository.save(salePromotion);
    }

    public SalePromotion approve(Long id) {
        SalePromotion salePromotion = getById(id);
        salePromotion.approve();
        return salePromotionRepository.save(salePromotion);
    }

    public SalePromotion reject(Long id) {
        SalePromotion salePromotion = getById(id);
        salePromotion.reject();
        return salePromotionRepository.save(salePromotion);
    }
}