package com.example.demo.entity;

import com.example.demo.entity.Enum.PaymentEnums;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    Date createAt;

    @Enumerated(EnumType.STRING)
    PaymentEnums payment_method;

    @OneToOne
    @JoinColumn(name = "order_id")
            @JsonIgnore
    Orders orders;



    @OneToMany(mappedBy = "payment",cascade = CascadeType.ALL)
            @JsonIgnore
    Set<Transactions> transactions;


}
