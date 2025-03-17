package com.example.demo.model.Response;

import com.example.demo.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class UserResponse {

    List<User> content;
    int pageNumber;
    int totalElements;
    int totalPages;


}
