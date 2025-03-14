package com.example.demo.controller;

import com.example.demo.entity.Product;
//import com.example.demo.service.FirebaseStorageService;
import com.example.demo.service.ImgurService;
import com.example.demo.service.ProductService;

import io.jsonwebtoken.io.IOException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

@RestController
@RequestMapping("/api/product")
@SecurityRequirement(name = "api")
@CrossOrigin(origins = "*")
public class ProductAPI {

    @Autowired
    ProductService productService;




    @PostMapping
    
    public ResponseEntity create(@RequestBody Product product) {
      Product newProduct = productService.create(product);
        return ResponseEntity.ok(newProduct);
    }


    @GetMapping
    public ResponseEntity getAll() {
        List<Product> products = productService.getAll();
        return ResponseEntity.ok(products);
    }
    @Autowired
    private ImgurService imgurService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile image) {
        try {
            // Convert MultipartFile to File
            File file = convertMultiPartToFile(image);
            String response = imgurService.uploadImage(file);
            file.delete(); // Delete the temporary file
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Error occurred while uploading", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException, java.io.IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }
    @PutMapping("{id}")

    public ResponseEntity update(@PathVariable Long id, @RequestBody Product product) throws Exception {
        Product updateProduct = productService.update(id, product);
        return ResponseEntity.ok(updateProduct);
    }

    @DeleteMapping("{Id}")

    public ResponseEntity deleteProduct(@PathVariable Long Id){
        Product deleteProduct = productService.delete(Id);
        return ResponseEntity.ok(deleteProduct);
    }
}