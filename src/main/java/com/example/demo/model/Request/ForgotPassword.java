package com.example.demo.model.Request;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class ForgotPassword {
    @Email(message = "Invalid Email")
    String email;
}
