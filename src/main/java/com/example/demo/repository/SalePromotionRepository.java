package com.example.demo.repository;

import com.example.demo.entity.SalePromotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalePromotionRepository extends JpaRepository<SalePromotion, Long> {
}