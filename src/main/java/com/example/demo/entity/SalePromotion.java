package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class SalePromotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    double discountPercentage;
    double discountAmount;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date endAt;

    boolean status;
    boolean pending;

    @ManyToMany
    @JoinTable(
            name = "product_sale_promotion",
            joinColumns = @JoinColumn(name = "sale_promotion_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )

    private List<Product> products;

    public void approve() {
        this.status = true;
        this.pending = false;
    }

    public void reject() {
        this.status = false;
        this.pending = false;
    }
}
