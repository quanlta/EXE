package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter

public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Date date;

    float total;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    Account customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    List<OrderDetail> orderDetail;

    @ManyToMany(mappedBy = "orders")
    @JsonIgnore
    List<Product> products;

    @OneToOne(mappedBy = "orders")
    @JsonIgnore
    Payment payment;
}
