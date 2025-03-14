package com.example.demo.model.Response;

import com.example.demo.entity.Enum.Role;
import lombok.Data;

@Data
public class AccountResponse {
    Long Id;
    String Username;
    String Password;
    String name;
    String Email;
    String Phone;
    String Token;
    Role role;
}
