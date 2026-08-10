package com.soumya.ecommerce.mapper;

import com.soumya.ecommerce.dto.CartDTO;
import com.soumya.ecommerce.dto.CartItemDTO;
import com.soumya.ecommerce.entity.Cart;
import com.soumya.ecommerce.entity.CartItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class CartMapper {

    public CartDTO toDto(Cart cart) {

        List<CartItemDTO> itemDtos = cart.getItems().stream()
                .map(this::toItemDto)
                .toList();

        BigDecimal total = itemDtos.stream()
                .map(CartItemDTO::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return CartDTO.builder()
                .id(cart.getId())
                .items(itemDtos)
                .totalAmount(total)
                .build();
    }

    public CartItemDTO toItemDto(CartItem item) {

        BigDecimal subtotal = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));

        return CartItemDTO.builder()
                .id(item.getId())
                .productId(item.getProductVariant().getProduct().getId())
                .productName(item.getProductVariant().getProduct().getName())
                .productVariantId(item.getProductVariant().getId())
                .color(item.getProductVariant().getColor())
                .size(item.getProductVariant().getSize())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .subtotal(subtotal)
                .build();
    }
}
