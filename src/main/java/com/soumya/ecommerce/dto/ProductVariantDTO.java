package com.soumya.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariantDTO {

    private UUID id;

    @NotBlank(message = "color is required")
    private String color;

    @NotBlank(message = "size is required")
    private String size;

    @NotNull(message = "stockQuantity is required")
    @PositiveOrZero(message = "stockQuantity cannot be negative")
    private Integer stockQuantity;
}
