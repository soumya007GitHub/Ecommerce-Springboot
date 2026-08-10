package com.soumya.ecommerce.service;

import com.soumya.ecommerce.dto.ProductDTO;
import com.soumya.ecommerce.entity.Category;
import com.soumya.ecommerce.entity.CategoryType;
import com.soumya.ecommerce.entity.Product;
import com.soumya.ecommerce.exception.ResourceNotFoundException;
import com.soumya.ecommerce.mapper.ProductMapper;
import com.soumya.ecommerce.repository.CategoryRepository;
import com.soumya.ecommerce.repository.CategoryTypeRepository;
import com.soumya.ecommerce.repository.ProductRepository;
import com.soumya.ecommerce.specification.ProductSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryTypeRepository categoryTypeRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductDTO addProduct(ProductDTO productDTO) {

        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> ResourceNotFoundException.of("Category", productDTO.getCategoryId()));

        CategoryType categoryType = categoryTypeRepository.findById(productDTO.getCategoryTypeId())
                .orElseThrow(() -> ResourceNotFoundException.of("CategoryType", productDTO.getCategoryTypeId()));

        Product product = productMapper.toEntity(productDTO, category, categoryType);

        Product savedProduct = productRepository.save(product);

        return productMapper.toDto(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDTO getProductById(UUID productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> ResourceNotFoundException.of("Product", productId));

        return productMapper.toDto(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> getProducts(
            UUID categoryId,
            UUID categoryTypeId,
            String brand,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            String keyword,
            Pageable pageable
    ) {

        Page<Product> products = productRepository.findAll(
                ProductSpecification.filterBy(categoryId, categoryTypeId, brand, minPrice, maxPrice, keyword),
                pageable
        );

        return products.map(productMapper::toDto);
    }

    @Override
    public ProductDTO updateProduct(UUID productId, ProductDTO productDTO) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> ResourceNotFoundException.of("Product", productId));

        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> ResourceNotFoundException.of("Category", productDTO.getCategoryId()));

        CategoryType categoryType = categoryTypeRepository.findById(productDTO.getCategoryTypeId())
                .orElseThrow(() -> ResourceNotFoundException.of("CategoryType", productDTO.getCategoryTypeId()));

        productMapper.updateEntity(product, productDTO, category, categoryType);

        Product updatedProduct = productRepository.save(product);

        return productMapper.toDto(updatedProduct);
    }

    @Override
    public void deleteProduct(UUID productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> ResourceNotFoundException.of("Product", productId));

        productRepository.delete(product);
    }
}
