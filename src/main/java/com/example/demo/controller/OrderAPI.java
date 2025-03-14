package com.example.demo.controller;

import com.example.demo.entity.Account;
import com.example.demo.entity.Orders;
import com.example.demo.model.Request.OrderRequest;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.AuthenticationService;
import com.example.demo.service.OrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@SecurityRequirement(name = "api")
@CrossOrigin(origins = "*")
public class OrderAPI {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    AuthenticationService authenticationService;
    @PostMapping
    public ResponseEntity create(@RequestBody OrderRequest orderRequest) throws Exception {
        String vnPayURL  = orderService.createUrl(orderRequest);
        return ResponseEntity.ok(vnPayURL);
    }
    @GetMapping
    public ResponseEntity getAll(){
        Account account = authenticationService.getCurrentAccount();
        List<Orders> orders = orderRepository.findOrdersByCustomer(account);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("{id}")
    public ResponseEntity get(@PathVariable Long id){
        Orders orders = orderRepository.findOrdersById(id);
        return ResponseEntity.ok(orders);
    }
    @PutMapping("{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody OrderRequest orderRequest) {
        Orders orders = orderService.update(id, orderRequest);
        return ResponseEntity.ok(orders);
    }
    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id){
        orderService.delete(id);
    }

    @PostMapping("/transaction")
    public ResponseEntity createTransaction(@RequestParam Long orderId) {
        orderService.createTransaction(orderId);
        return ResponseEntity.ok("success");
    }


    @GetMapping("/payment-callback/{orderId}")
    public void paymentCallback(
            @PathVariable Long orderId,
            @RequestParam Map<String, String> queryParams,
            HttpServletResponse response) throws IOException {

        // Giải mã (decode) các tham số
        String status = URLDecoder.decode(queryParams.get("vnp_TransactionStatus"), StandardCharsets.UTF_8.name());
        String orderInfo = URLDecoder.decode(queryParams.get("vnp_OrderInfo"), StandardCharsets.UTF_8.name());
        String amount = URLDecoder.decode(queryParams.get("vnp_Amount"), StandardCharsets.UTF_8.name());
        String payDate = URLDecoder.decode(queryParams.get("vnp_PayDate"), StandardCharsets.UTF_8.name());

        // Tạo URL chuyển hướng đến FE với các tham số đã decode
        String redirectUrl = String.format(
                "http://localhost:5173/payment?status=%s&OrderInfo=%s&Amount=%s&payDate=%s",
                status, orderInfo, amount, payDate);

        // Chuyển hướng người dùng đến URL của Front-end
        response.sendRedirect(redirectUrl);
    }
}
