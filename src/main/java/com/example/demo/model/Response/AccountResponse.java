package com.example.demo.model.Response;

import com.example.demo.entity.Enum.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class AccountResponse {
    Long Id;
    String Username;
    String Password;
    String address;
    String Email;
    String Phone;
    String Token;

    @Enumerated(EnumType.STRING)
    Role role;
}
