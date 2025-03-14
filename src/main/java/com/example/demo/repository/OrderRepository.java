package com.example.demo.repository;

import com.example.demo.entity.Account;
import com.example.demo.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
   List<Orders> findOrdersByCustomer(Account customer);
    Orders findOrdersById(Long id);
}
