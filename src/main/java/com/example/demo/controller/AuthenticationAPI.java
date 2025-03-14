package com.example.demo.controller;

import com.example.demo.entity.Account;
import com.example.demo.model.Request.*;
import com.example.demo.model.Response.AccountResponse;
import com.example.demo.service.AuthenticationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account")
@SecurityRequirement(name = "api")
@CrossOrigin("*")
public class AuthenticationAPI {

    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity register(@Valid @RequestBody RegisterRequest registerRequest) {
        AccountResponse newAccount = authenticationService.register(registerRequest);
        return ResponseEntity.ok(newAccount);
    }

    @PostMapping("login")
    public ResponseEntity login(@Valid @RequestBody LoginRequest loginRequest) {
        AccountResponse newAccount = authenticationService.login(loginRequest);
        return ResponseEntity.ok(newAccount);
    }

    @GetMapping("/id")
    public ResponseEntity getAccountById(@RequestParam Long id) {
        Account account = authenticationService.getAccountById(id);
        return ResponseEntity.ok(account);
    }

    @GetMapping

    public ResponseEntity getAllAccount() {
        List<Account> accounts = authenticationService.getAllAccount();
        return ResponseEntity.ok(accounts);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity forgotPassword(@Valid @RequestBody ForgotPassword forgotPassword) {
        authenticationService.forgotPassword(forgotPassword.getEmail());
        return ResponseEntity.ok("Check your email to confirm reset password");
    }

    @PostMapping("/reset-password")
    public ResponseEntity resetPassword(@Valid @RequestBody ResetPassword resetPassword) {
        authenticationService.resetPassword(resetPassword);
        return ResponseEntity.ok("Password reset successfully");
    }
    @PutMapping("/account/{id}")

    public ResponseEntity updateAccount(@PathVariable Long id, @Valid @RequestBody UpdateAccountRequest updateAccountRequest) {
        AccountResponse updatedAccount = authenticationService.updateAccount(id, updateAccountRequest);
        return ResponseEntity.ok(updatedAccount);
    }

}