package com.example.demo.service;

import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;
//
//    @Autowired
//    FirebaseStorageService firebaseStorageService;

    public Product create(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getAll() {
        List<Product> products = productRepository.findAll();
        return products;
    }

    public Product getProductById(Long id) {
        return productRepository.findProductById(id);
    }

    public Product update(Long id, Product product) throws IOException {

        Product updateProduct = productRepository.findProductById(id);
        updateProduct.setName(product.getName());
        updateProduct.setDescription(product.getDescription());
        updateProduct.setPrice(product.getPrice());
        updateProduct.setImage(product.getImage());
//        if (file != null && !file.isEmpty()) {
//            String url = firebaseStorageService.uploadFile(file);
//            updateProduct.setImage(url);
//        }
        return productRepository.save(updateProduct);


    }

    public Product delete(Long id) {


        Product product = productRepository.findProductById(id);
        if (product == null)
            throw new EntityNotFoundException("User not found");
        else {

            productRepository.delete(product);
            return product;
        }

    }


}