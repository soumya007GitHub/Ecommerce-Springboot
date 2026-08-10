package com.soumya.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariantDTO {

    private UUID id;

    private String color;

    private String size;

    private Integer stockQuantity;
}