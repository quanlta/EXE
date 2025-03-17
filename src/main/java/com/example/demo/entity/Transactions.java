package com.example.demo.entity;

import com.example.demo.entity.Enum.TransactionsEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "from_id")
    Account from;

    @ManyToOne
    @JoinColumn(name = "to_id")
    Account to;

    @ManyToOne
    @JoinColumn(name = "payment_id")
            @JsonIgnore
    Payment payment;
    @Enumerated(EnumType.STRING)
    TransactionsEnum status;

    String description;
}