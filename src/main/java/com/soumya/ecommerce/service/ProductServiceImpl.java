package com.soumya.ecommerce.service;

import com.soumya.ecommerce.dto.ProductDTO;
import com.soumya.ecommerce.entity.Product;
import com.soumya.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public ProductDTO addProduct(ProductDTO productDTO) {

        Product product = new Product();

        product.setName(productDTO.getName());

        Product savedProduct = productRepository.save(product);

        ProductDTO response = new ProductDTO();

        response.setId(savedProduct.getId());
        response.setName(savedProduct.getName());

        return response;
    }

    @Override
    public List<ProductDTO> getProducts() {

        List<Product> products = productRepository.findAll();

        return products.stream()
                .map(product -> {

                    ProductDTO productDTO = new ProductDTO();

                    productDTO.setId(product.getId());
                    productDTO.setName(product.getName());

                    return productDTO;

                }).toList();
    }
}