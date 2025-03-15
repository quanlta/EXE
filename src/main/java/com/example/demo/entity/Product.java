package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String image;

    @NotBlank(message = "Name is mandatory")
    String name;

    String description;
    String price;
    String createdBy;

    @Enumerated(EnumType.STRING)
    Status status;

    @Enumerated(EnumType.STRING)
    Pending pending;

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    List<OrderDetail> orderDetails;

    @ManyToOne
    @JoinColumn(name = "account_id")
    @JsonIgnore
    Account account;

    @ManyToMany
    @JoinTable(
            name = "product_orders",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "order_id")
    )
    @JsonIgnore
    List<Orders> orders;

    @ManyToMany(mappedBy = "products")
    @JsonIgnore
    private List<SalePromotion> salePromotions;


    public enum Status {
        TRUE, FALSE;

        @JsonCreator
        public static Status fromString(String value) {
            return value != null ? Status.valueOf(value.toUpperCase()) : null;
        }
    }

    public enum Pending {
        TRUE, FALSE;

        @JsonCreator
        public static Pending fromString(String value) {
            return value != null ? Pending.valueOf(value.toUpperCase()) : null;
        }
    }

    public void approve() {
        this.status = Status.TRUE;
        this.pending = Pending.FALSE;
    }

    public void reject() {
        this.status = Status.FALSE;
        this.pending = Pending.FALSE;
    }

    public void requestApproval() {
        this.pending = Pending.TRUE;
    }
}
