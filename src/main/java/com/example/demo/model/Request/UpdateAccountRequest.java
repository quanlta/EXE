package com.example.demo.model.Request;

import com.example.demo.entity.Enum.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;



@Data
public class UpdateAccountRequest {
    String username;
    String email;
    String phone;
    String password;
    @Enumerated(EnumType.STRING)
    Role role;
}