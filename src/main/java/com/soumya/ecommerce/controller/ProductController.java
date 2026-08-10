package com.soumya.ecommerce.controller;

import com.soumya.ecommerce.dto.ProductDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @GetMapping
    public ResponseEntity<?> getAllProducts() {

        return ResponseEntity.ok("Get All Products API");
    }
    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductDetails(
            @PathVariable Long productId) {

        return ResponseEntity.ok("Get Product Details API : " + productId);
    }
    @PostMapping
    public ResponseEntity<?> addProduct(
            @RequestBody ProductDTO productDTO) {

        return ResponseEntity.ok("Add Product API");
    }

    @PutMapping("/{productId}")
    public ResponseEntity<?> updateProduct(
            @PathVariable Long productId,
            @RequestBody ProductDTO productDTO) {

        return ResponseEntity.ok("Update Product API : " + productId);
    }


    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(
            @PathVariable Long productId) {

        return ResponseEntity.ok("Delete Product API : " + productId);
    }

}