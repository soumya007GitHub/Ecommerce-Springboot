package com.soumya.ecommerce.dto;

import com.soumya.ecommerce.entity.OrderStatus;
import com.soumya.ecommerce.entity.PaymentMethod;
import com.soumya.ecommerce.entity.PaymentStatus;
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
public class OrderDTO {

    private UUID id;

    private List<OrderItemDTO> items;

    private BigDecimal totalAmount;

    private OrderStatus status;

    private PaymentStatus paymentStatus;

    private PaymentMethod paymentMethod;

    private String shippingAddressLine1;

    private String shippingAddressLine2;

    private String shippingCity;

    private String shippingState;

    private String shippingPostalCode;

    private String shippingCountry;

    private String shippingContactPhone;

    private LocalDateTime createdAt;
}
