package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.entity.Enum.PaymentEnums;
import com.example.demo.entity.Enum.Role;
import com.example.demo.entity.Enum.TransactionsEnum;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.model.Request.OrderDetailRequest;
import com.example.demo.model.Request.OrderRequest;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PaymentRepository paymentRepository;



    public Orders create(OrderRequest orderRequest) {

        Account customer = authenticationService.getCurrentAccount();

        Orders order = new Orders();
        List<OrderDetail> orderDetails = new ArrayList<>();
        float total = 0;

        order.setCustomer(customer);
        order.setDate(new Date());

        for (OrderDetailRequest orderDetailRequest : orderRequest.getDetail()) {

            Product product = productRepository.findProductById(orderDetailRequest.getProductId());
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setQuantity(orderDetailRequest.getQuantity());
            orderDetail.setPrice(Float.parseFloat(product.getPrice()));
            orderDetail.setProduct(product);
            orderDetail.setOrder(order);


            orderDetails.add(orderDetail);

            total += Float.parseFloat(product.getPrice()) * orderDetailRequest.getQuantity();

        }
        order.setOrderDetail(orderDetails);
        order.setTotal(total);

        return orderRepository.save(order);

    }
    public List<Orders> getAll() {
        Account customer = authenticationService.getCurrentAccount();
        List<Orders> orders = orderRepository.findOrdersByCustomer(customer);
        if (orders == null || orders.isEmpty()) {
            throw new EntityNotFoundException("No orders found for the current customer");
        }
        return orders;
    }

    public Orders get(Long id) {
        Orders order = orderRepository.findOrdersById(id);
        if (order == null) {
            throw new EntityNotFoundException("Order not found");
        }
        return order;
    }
    public Orders update(Long id, OrderRequest orderRequest) {
        Orders order = orderRepository.findOrdersById(id);
        List<OrderDetail> orderDetails = new ArrayList<>();
        float total = 0;
        for (OrderDetailRequest orderDetailRequest : orderRequest.getDetail()) {
            Product product = productRepository.findProductById(orderDetailRequest.getProductId());
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setQuantity(orderDetailRequest.getQuantity());
            orderDetail.setPrice(Float.parseFloat(product.getPrice()));
            orderDetail.setProduct(product);
            orderDetail.setOrder(order);
            orderDetails.add(orderDetail);
            total += Float.parseFloat(product.getPrice()) * orderDetailRequest.getQuantity();
        }
        order.setOrderDetail(orderDetails);
        order.setTotal(total);
        return orderRepository.save(order);
    }

    public void delete(Long id) {
        Orders order = orderRepository.findOrdersById(id);
        orderRepository.delete(order);
    }

    public String createUrl(OrderRequest orderRequest) throws  Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime createDate = LocalDateTime.now();
        String formattedCreateDate = createDate.format(formatter);


        // create order
        Orders orders = create(orderRequest);

        float money = orders.getTotal() * 100;
        String amount = String.valueOf((int) money);


        String tmnCode = "0I712H9B";
        String secretKey = "ZOPSQ8G5KQFVU2PDYNEA0VB05BQUVSZO";
        String vnpUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
        String returnUrl = "http://localhost:8080/api/orders/payment-callback/" + orders.getId(); //frontend
        String currCode = "VND";

        Map<String, String> vnpParams = new TreeMap<>();
        vnpParams.put("vnp_Version", "2.1.0");
        vnpParams.put("vnp_Command", "pay");
        vnpParams.put("vnp_TmnCode", tmnCode);
        vnpParams.put("vnp_Locale", "vn");
        vnpParams.put("vnp_CurrCode", currCode);
        vnpParams.put("vnp_TxnRef", orders.getId().toString());
        vnpParams.put("vnp_OrderInfo", "Thanh toan cho ma GD: " + orders.getId());
        vnpParams.put("vnp_OrderType", "other");
        vnpParams.put("vnp_Amount",amount);

        vnpParams.put("vnp_ReturnUrl", returnUrl);
        vnpParams.put("vnp_CreateDate", formattedCreateDate);
        vnpParams.put("vnp_IpAddr", "128.199.178.23");

        StringBuilder signDataBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : vnpParams.entrySet()) {
            signDataBuilder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.toString()));
            signDataBuilder.append("=");
            signDataBuilder.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.toString()));
            signDataBuilder.append("&");
        }
        signDataBuilder.deleteCharAt(signDataBuilder.length() - 1); // Remove last '&'

        String signData = signDataBuilder.toString();
        String signed = generateHMAC(secretKey, signData);

        vnpParams.put("vnp_SecureHash", signed);

        StringBuilder urlBuilder = new StringBuilder(vnpUrl);
        urlBuilder.append("?");
        for (Map.Entry<String, String> entry : vnpParams.entrySet()) {
            urlBuilder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.toString()));
            urlBuilder.append("=");
            urlBuilder.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.toString()));
            urlBuilder.append("&");
        }
        urlBuilder.deleteCharAt(urlBuilder.length() - 1); // Remove last '&'

        return urlBuilder.toString();
    }
    private String generateHMAC(String secretKey, String signData) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac hmacSha512 = Mac.getInstance("HmacSHA512");
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
        hmacSha512.init(keySpec);
        byte[] hmacBytes = hmacSha512.doFinal(signData.getBytes(StandardCharsets.UTF_8));

        StringBuilder result = new StringBuilder();
        for (byte b : hmacBytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    public void createTransaction(Long uuid) {
        // find order

        Orders order = orderRepository.findById(uuid).orElseThrow(()->new EntityNotFoundException("Order not found"));
        // create payment
        Payment payment = new Payment();
        payment.setOrders(order);
        payment.setCreateAt(new Date());
        payment.setPayment_method(PaymentEnums.BANKING);;


        Set<Transactions> setTransactions = new HashSet<>();


        // create transaction
        Transactions transactions1 = new Transactions();
        //VNPay -> Member
        Account user = authenticationService.getCurrentAccount();
        transactions1.setFrom(null);
        transactions1.setTo(user);
        transactions1.setPayment(payment);
        transactions1.setStatus(TransactionsEnum.SUCCESS);
        transactions1.setDescription("VNPay to User");

        setTransactions.add(transactions1);

        Transactions transactions2 = new Transactions();
        //VNPay -> ADMIN
        Account admin = accountRepository.findAccountByRole(Role.ADMIN);
        transactions2.setFrom(user);
        transactions2.setTo(admin);
        transactions2.setPayment(payment);
        transactions2.setStatus(TransactionsEnum.SUCCESS);
        transactions2.setDescription("User to Admin");
        float newBalance = admin.getBalance() + order.getTotal();
        admin.setBalance(newBalance);


        setTransactions.add(transactions2);




        accountRepository.save(admin);
        payment.setTransactions(setTransactions);
        paymentRepository.save(payment);


    }
}
