package com.example.demo.model.Request;

import lombok.Data;

@Data
public class FeedbackRequest {
    String content;
    int rating;
    Long shopId;
}
