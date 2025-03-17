package com.example.demo.model.Request;

import com.example.demo.entity.Enum.Role;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserRequest {
    String username;


    String password;

    Role role;





    @Pattern(regexp = "(84|0[3|5|7|8|9])+(\\d{8})\\b", message = "Phone number is invalid")
    String phone;



    @Column(name = "Email")
    String email;

}
