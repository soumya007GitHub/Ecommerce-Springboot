package com.soumya.ecommerce.controller;

import com.soumya.ecommerce.dto.ProductDTO;
import com.soumya.ecommerce.service.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductServiceImpl productService;
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getProducts());
    }
//    @GetMapping("/{productId}")
//    public ResponseEntity<ProductDTO> getProductDetails(
//            @PathVariable Long productId) {
//
//        return ResponseEntity.status(HttpStatus.OK).body();
//    }
    @PostMapping
    public ResponseEntity<ProductDTO> addProduct(
            @RequestBody ProductDTO productDTO) {

        return ResponseEntity.ok(productService.addProduct(productDTO));
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