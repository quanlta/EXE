package com.example.demo.model.Request;

import lombok.Data;

@Data
public class OrderDetailRequest {


    Long shopId;
    Long productId;
        int quantity;
    }

