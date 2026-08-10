package com.soumya.ecommerce.service;

import com.soumya.ecommerce.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.UUID;

public interface ProductService {

    ProductDTO addProduct(ProductDTO productDTO);

    ProductDTO getProductById(UUID productId);

    Page<ProductDTO> getProducts(
            UUID categoryId,
            UUID categoryTypeId,
            String brand,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            String keyword,
            Pageable pageable
    );

    ProductDTO updateProduct(UUID productId, ProductDTO productDTO);

    void deleteProduct(UUID productId);
}
