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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryTypeRepository categoryTypeRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    private Category category;
    private CategoryType categoryType;
    private ProductDTO productDTO;

    @BeforeEach
    void setUp() {

        category = new Category();
        category.setId(UUID.randomUUID());

        categoryType = new CategoryType();
        categoryType.setId(UUID.randomUUID());

        productDTO = ProductDTO.builder()
                .name("Running Shoe")
                .brand("Nike")
                .price(BigDecimal.valueOf(2999))
                .categoryId(category.getId())
                .categoryTypeId(categoryType.getId())
                .build();
    }

    @Test
    void addProduct_savesAndReturnsMappedProduct() {

        Product product = new Product();
        Product savedProduct = new Product();
        savedProduct.setId(UUID.randomUUID());

        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(categoryTypeRepository.findById(categoryType.getId())).thenReturn(Optional.of(categoryType));
        when(productMapper.toEntity(productDTO, category, categoryType)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(savedProduct);
        when(productMapper.toDto(savedProduct)).thenReturn(
                ProductDTO.builder().id(savedProduct.getId()).name("Running Shoe").build()
        );

        ProductDTO result = productService.addProduct(productDTO);

        assertThat(result.getId()).isEqualTo(savedProduct.getId());
        verify(productRepository).save(product);
    }

    @Test
    void addProduct_throwsWhenCategoryMissing() {

        when(categoryRepository.findById(category.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.addProduct(productDTO))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getProductById_throwsWhenNotFound() {

        UUID productId = UUID.randomUUID();

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getProductById(productId))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void deleteProduct_removesExistingProduct() {

        UUID productId = UUID.randomUUID();
        Product product = new Product();
        product.setId(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        productService.deleteProduct(productId);

        verify(productRepository).delete(product);
    }
}
