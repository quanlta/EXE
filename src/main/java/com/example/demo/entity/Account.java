package com.example.demo.entity;

import com.example.demo.entity.Enum.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
@Table (name = "Account")
public class Account implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    long id;

    float balance = 0;

    @Enumerated(EnumType.STRING)
    Role role;

    @NotBlank(message = "Username can not be blank!")
    @Column(name = "Username")
    String username;

    String name;

    @Size(min = 6, message = "Password must be at least 6 characters")
    @Column(name = "Password")
    String password;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Invalid Email!")
    @Column(name = "Email")
    String email;

    @Pattern(regexp = "(84|0[3|5|7|8|9])+(\\d{8})", message = "Invalid phone")
    String phone;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if(this.role != null) authorities.add(new SimpleGrantedAuthority(this.role.toString()));

        return authorities;

    }
    @Override
    public String getUsername(){
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    @OneToMany(mappedBy = "account")
    @JsonIgnore

    List<User> users;


    @OneToMany(mappedBy = "customer")
    @JsonIgnore
    List<Orders> orders;


    @OneToMany(mappedBy = "from")
    @JsonIgnore
    List<Transactions> transactionsFrom;

    @OneToMany(mappedBy = "to")
    @JsonIgnore
    List<Transactions> transactionsTo;

    @OneToMany(mappedBy = "account")
    @JsonIgnore
    List<Product> products;







}
