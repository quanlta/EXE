package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Entity
@Getter
@Setter
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    int quantity;
    float price;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore
     Orders order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    Product product;


}