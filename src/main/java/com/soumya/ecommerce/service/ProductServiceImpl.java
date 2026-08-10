package com.soumya.ecommerce.service;

import com.soumya.ecommerce.dto.ProductDTO;
import com.soumya.ecommerce.dto.ProductResourceDTO;
import com.soumya.ecommerce.dto.ProductVariantDTO;
import com.soumya.ecommerce.entity.Product;
import com.soumya.ecommerce.entity.ProductResource;
import com.soumya.ecommerce.entity.ProductVariant;
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

    @Override
    public Product mapToProductEntity(ProductDTO productDTO) {

        Product product = new Product();

        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setBrand(productDTO.getBrand());
        product.setIsNewArrival(productDTO.isNewArrival());

        product.setProductVariantList(
                mapToProductVariants(
                        productDTO.getProductVariants(),
                        product
                )
        );

        product.setListResources(
                mapToProductResources(
                        productDTO.getProductResources(),
                        product
                )
        );

        return product;
    }

    @Override
    public ProductDTO mapToProductDTO(Product product) {

        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .brand(product.getBrand())
                .isNewArrival(product.getIsNewArrival())
                .productVariants(
                        mapToProductVariantDTOs(
                                product.getProductVariantList()
                        )
                )
                .productResources(
                        mapToProductResourceDTOs(
                                product.getListResources()
                        )
                )
                .build();
    }

    @Override
    public List<ProductVariant> mapToProductVariants(
            List<ProductVariantDTO> productVariantDTOList,
            Product product
    ) {

        if (productVariantDTOList == null) {
            return List.of();
        }

        return productVariantDTOList.stream()
                .map(productVariantDTO -> {

                    ProductVariant productVariant = new ProductVariant();

                    productVariant.setColor(productVariantDTO.getColor());
                    productVariant.setSize(productVariantDTO.getSize());
                    productVariant.setStockQuantity(
                            productVariantDTO.getStockQuantity()
                    );

                    productVariant.setProduct(product);

                    return productVariant;

                })
                .toList();
    }

    @Override
    public List<ProductVariantDTO> mapToProductVariantDTOs(
            List<ProductVariant> productVariants
    ) {

        if (productVariants == null) {
            return List.of();
        }

        return productVariants.stream()
                .map(productVariant -> {
                            ProductVariantDTO productVariantDTO = new ProductVariantDTO();
                    productVariantDTO.setId(productVariant.getId());
                    productVariantDTO.setColor(productVariant.getColor());
                    productVariantDTO.setSize(productVariant.getSize());
                    productVariantDTO.setStockQuantity(productVariant.getStockQuantity());
                    return productVariantDTO;
                        }
                )
                .toList();
    }

    @Override
    public List<ProductResource> mapToProductResources(
            List<ProductResourceDTO> productResourceDTOList,
            Product product
    ) {

        if (productResourceDTOList == null) {
            return List.of();
        }

        return productResourceDTOList.stream()
                .map(productResourceDTO -> {

                    ProductResource productResource = new ProductResource();

                    productResource.setName(productResourceDTO.getName());
                    productResource.setUrl(productResourceDTO.getUrl());
                    productResource.setType(productResourceDTO.getType());
                    productResource.setIsPrimary(
                            productResourceDTO.isPrimary()
                    );

                    productResource.setProduct(product);

                    return productResource;

                })
                .toList();
    }

    @Override
    public List<ProductResourceDTO> mapToProductResourceDTOs(
            List<ProductResource> productResources
    ) {

        if (productResources == null) {
            return List.of();
        }

        return productResources.stream()
                .map(productResource ->
                        ProductResourceDTO.builder()
                                .id(productResource.getId())
                                .name(productResource.getName())
                                .url(productResource.getUrl())
                                .type(productResource.getType())
                                .isPrimary(productResource.getIsPrimary())
                                .build()
                )
                .toList();
    }
}