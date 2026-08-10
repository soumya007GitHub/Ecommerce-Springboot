package com.soumya.ecommerce.mapper;

import com.soumya.ecommerce.dto.OrderDTO;
import com.soumya.ecommerce.dto.OrderItemDTO;
import com.soumya.ecommerce.entity.Order;
import com.soumya.ecommerce.entity.OrderItem;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public OrderDTO toDto(Order order) {

        return OrderDTO.builder()
                .id(order.getId())
                .items(order.getItems().stream().map(this::toItemDto).toList())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .paymentStatus(order.getPaymentStatus())
                .paymentMethod(order.getPaymentMethod())
                .shippingAddressLine1(order.getShippingAddressLine1())
                .shippingAddressLine2(order.getShippingAddressLine2())
                .shippingCity(order.getShippingCity())
                .shippingState(order.getShippingState())
                .shippingPostalCode(order.getShippingPostalCode())
                .shippingCountry(order.getShippingCountry())
                .shippingContactPhone(order.getShippingContactPhone())
                .createdAt(order.getCreatedAt())
                .build();
    }

    private OrderItemDTO toItemDto(OrderItem item) {

        return OrderItemDTO.builder()
                .id(item.getId())
                .productId(item.getProductId())
                .productName(item.getProductName())
                .productVariantId(item.getProductVariantId())
                .color(item.getColor())
                .size(item.getSize())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .subtotal(item.getUnitPrice().multiply(java.math.BigDecimal.valueOf(item.getQuantity())))
                .build();
    }
}
