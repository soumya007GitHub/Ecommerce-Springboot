package com.soumya.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDTO {

    private UUID id;

    private UUID productId;

    private String productName;

    private UUID productVariantId;

    private String color;

    private String size;

    private Integer quantity;

    private BigDecimal unitPrice;

    private BigDecimal subtotal;
}
