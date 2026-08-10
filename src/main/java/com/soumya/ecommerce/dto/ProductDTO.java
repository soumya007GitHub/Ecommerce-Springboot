package com.soumya.ecommerce.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {

    private UUID id;

    @NotBlank(message = "name is required")
    private String name;

    private String description;

    @NotNull(message = "price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "price must be greater than 0")
    private BigDecimal price;

    @NotBlank(message = "brand is required")
    private String brand;

    private boolean isNewArrival;

    @NotNull(message = "categoryId is required")
    private UUID categoryId;

    @NotNull(message = "categoryTypeId is required")
    private UUID categoryTypeId;

    private Float rating;

    @Valid
    private List<ProductVariantDTO> productVariants;

    @Valid
    private List<ProductResourceDTO> productResources;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
