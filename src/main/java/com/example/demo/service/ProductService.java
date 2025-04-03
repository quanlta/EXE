package com.example.demo.service;

import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product create(Product product, String userRole) {
        if ("OWNER".equalsIgnoreCase(userRole)) {
            product.setStatus(Product.Status.TRUE);
            product.setPending(Product.Pending.FALSE);
        } else {
            product.setStatus(Product.Status.TRUE);
            product.setPending(Product.Pending.TRUE);
        }
        return productRepository.save(product);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
    }

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public Product update(Long id, Product product) {
        Product existingProduct = getProductById(id);

        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setImage(product.getImage());
        existingProduct.setPending(Product.Pending.FALSE);
//        if (!"OWNER".equalsIgnoreCase(userRole)) {
//            existingProduct.setPending(Product.Pending.TRUE);
//        }

        return productRepository.save(existingProduct);
    }


    public Product changeStatus(Long id, String action) {
        Product product = getProductById(id);

        switch (action.toLowerCase()) {
            case "approve":
                product.setPending(Product.Pending.FALSE);
                break;
            case "reject":
                product.setPending(Product.Pending.FALSE);
                break;
            default:
                throw new IllegalArgumentException("Invalid action: " + action);
        }

        return productRepository.save(product);
    }

    private String getUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getAuthorities() != null) {
            return authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .findFirst()
                    .orElse("USER");
        }
        return "USER";
    }
}