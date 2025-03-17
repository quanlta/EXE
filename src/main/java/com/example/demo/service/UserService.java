package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.model.Request.UserRequest;
import com.example.demo.model.Response.UserResponse;
import com.example.demo.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AuthenticationService authenticationService;

    public User createUser(UserRequest userRequest) {
        try {
            User user = modelMapper.map(userRequest, User.class);
            return userRepository.save(user);
        } catch (Exception e) {
            throw new DuplicateEntity("Duplicate email");
        }
    }

    public User getUser(long id) {
        User user = userRepository.findUserById(id);
        if (user == null)
            throw new EntityNotFoundException("User not found");
        return user;
    }

    public UserResponse getAllUser(int page, int size) {
        Page<User> users = userRepository.findAll(PageRequest.of(page, size));
        return new UserResponse(users.getContent(), users.getNumber(), users.getNumberOfElements(), users.getTotalPages());
    }

    public User update(long id, User user) {
        User oldUser = userRepository.findUserById(id);
        if (oldUser == null)
            throw new EntityNotFoundException("User not found");

        oldUser.setUsername(user.getUsername());
        oldUser.setPassword(user.getPassword());
        oldUser.setEmail(user.getEmail());
        oldUser.setPhone(user.getPhone());

        return userRepository.save(oldUser);
    }

    public User delete(long id) {
        User user = userRepository.findUserById(id);
        if (user == null)
            throw new EntityNotFoundException("User not found");

        userRepository.delete(user);
        return user;
    }
}