package com.soumya.ecommerce.service;

import com.soumya.ecommerce.dto.ProductDTO;
import com.soumya.ecommerce.dto.ProductResourceDTO;
import com.soumya.ecommerce.dto.ProductVariantDTO;
import com.soumya.ecommerce.entity.Product;
import com.soumya.ecommerce.entity.ProductResource;
import com.soumya.ecommerce.entity.ProductVariant;

import java.util.List;

public interface ProductService {

    ProductDTO addProduct(ProductDTO productDTO);

    List<ProductDTO> getProducts();

    Product mapToProductEntity(ProductDTO productDTO);

    ProductDTO mapToProductDTO(Product product);

    List<ProductVariant> mapToProductVariants(
            List<ProductVariantDTO> productVariantDTOList,
            Product product
    );

    List<ProductVariantDTO> mapToProductVariantDTOs(
            List<ProductVariant> productVariants
    );

    List<ProductResource> mapToProductResources(
            List<ProductResourceDTO> productResourceDTOList,
            Product product
    );

    List<ProductResourceDTO> mapToProductResourceDTOs(
            List<ProductResource> productResources
    );
}