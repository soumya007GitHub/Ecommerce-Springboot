package com.soumya.ecommerce.mapper;

import com.soumya.ecommerce.dto.ProductDTO;
import com.soumya.ecommerce.dto.ProductResourceDTO;
import com.soumya.ecommerce.dto.ProductVariantDTO;
import com.soumya.ecommerce.entity.Category;
import com.soumya.ecommerce.entity.CategoryType;
import com.soumya.ecommerce.entity.Product;
import com.soumya.ecommerce.entity.ProductResource;
import com.soumya.ecommerce.entity.ProductVariant;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductMapper {

    public Product toEntity(ProductDTO dto, Category category, CategoryType categoryType) {

        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setBrand(dto.getBrand());
        product.setIsNewArrival(dto.isNewArrival());
        product.setCategory(category);
        product.setCategoryType(categoryType);
        product.setRating(0f);

        product.setProductVariantList(toVariantEntities(dto.getProductVariants(), product));
        product.setListResources(toResourceEntities(dto.getProductResources(), product));

        return product;
    }

    public void updateEntity(Product product, ProductDTO dto, Category category, CategoryType categoryType) {

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setBrand(dto.getBrand());
        product.setIsNewArrival(dto.isNewArrival());
        product.setCategory(category);
        product.setCategoryType(categoryType);

        product.getProductVariantList().clear();
        product.getProductVariantList().addAll(toVariantEntities(dto.getProductVariants(), product));

        product.getListResources().clear();
        product.getListResources().addAll(toResourceEntities(dto.getProductResources(), product));
    }

    public ProductDTO toDto(Product product) {

        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .brand(product.getBrand())
                .isNewArrival(Boolean.TRUE.equals(product.getIsNewArrival()))
                .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)
                .categoryTypeId(product.getCategoryType() != null ? product.getCategoryType().getId() : null)
                .rating(product.getRating())
                .productVariants(toVariantDtos(product.getProductVariantList()))
                .productResources(toResourceDtos(product.getListResources()))
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    private List<ProductVariant> toVariantEntities(List<ProductVariantDTO> dtos, Product product) {

        if (dtos == null) {
            return new java.util.ArrayList<>();
        }

        return dtos.stream()
                .map(dto -> {
                    ProductVariant variant = new ProductVariant();
                    variant.setColor(dto.getColor());
                    variant.setSize(dto.getSize());
                    variant.setStockQuantity(dto.getStockQuantity());
                    variant.setProduct(product);
                    return variant;
                })
                .collect(java.util.stream.Collectors.toCollection(java.util.ArrayList::new));
    }

    private List<ProductVariantDTO> toVariantDtos(List<ProductVariant> variants) {

        if (variants == null) {
            return List.of();
        }

        return variants.stream()
                .map(variant -> ProductVariantDTO.builder()
                        .id(variant.getId())
                        .color(variant.getColor())
                        .size(variant.getSize())
                        .stockQuantity(variant.getStockQuantity())
                        .build())
                .toList();
    }

    private List<ProductResource> toResourceEntities(List<ProductResourceDTO> dtos, Product product) {

        if (dtos == null) {
            return new java.util.ArrayList<>();
        }

        return dtos.stream()
                .map(dto -> {
                    ProductResource resource = new ProductResource();
                    resource.setName(dto.getName());
                    resource.setUrl(dto.getUrl());
                    resource.setType(dto.getType());
                    resource.setIsPrimary(dto.isPrimary());
                    resource.setProduct(product);
                    return resource;
                })
                .collect(java.util.stream.Collectors.toCollection(java.util.ArrayList::new));
    }

    private List<ProductResourceDTO> toResourceDtos(List<ProductResource> resources) {

        if (resources == null) {
            return List.of();
        }

        return resources.stream()
                .map(resource -> ProductResourceDTO.builder()
                        .id(resource.getId())
                        .name(resource.getName())
                        .url(resource.getUrl())
                        .type(resource.getType())
                        .isPrimary(Boolean.TRUE.equals(resource.getIsPrimary()))
                        .build())
                .toList();
    }
}
