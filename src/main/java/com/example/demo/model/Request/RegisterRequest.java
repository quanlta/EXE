package com.example.demo.model.Request;

import com.example.demo.entity.Enum.Role;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Username can not be blank!")
    @Column(name = "Username")
    String Username;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @Column(name = "Password")
    String Password;

    @NotBlank(message = "Email is mandatory")
    @jakarta.validation.constraints.Email(message = "Invalid Email!")
    @Column(name = "Email")
    String Email;

    @NotBlank(message = "Phone is mandatory")
    @Column(name = "Phone")
    @Pattern(regexp = "(84|0[3|5|7|8|9])+(\\d{8})", message = "Invalid phone")
    String Phone;

    Role role;

}
