package com.example.demo.model.Request;

import com.example.demo.entity.Account;
import lombok.Data;

@Data
public class EmailDetail {
    Account receiver;
    String subject;
    String link;
}
