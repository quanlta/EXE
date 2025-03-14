package com.example.demo.repository;

import com.example.demo.entity.Account;
import com.example.demo.entity.Enum.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findAccountByUsername(String username);

    Account findAccountById(long id);

    Account findAccountByEmail(String email);

    Account findAccountByRole(Role role);

}
