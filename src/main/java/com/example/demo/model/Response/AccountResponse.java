package com.example.demo.model.Response;

import com.example.demo.entity.Enum.Role;
import lombok.Data;

import java.util.List;

@Data
public class AccountResponse {
    Long Id;
    String Username;
    String Password;
    String Address;
    String Email;
    String Phone;
    String Token;
    Role role;
}
