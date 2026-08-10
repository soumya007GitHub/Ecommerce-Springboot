package com.soumya.ecommerce.service;

import com.soumya.ecommerce.dto.ProductDTO;

import java.util.List;

public interface ProductService {

    ProductDTO addProduct(ProductDTO productDTO);

    List<ProductDTO> getProducts();
}