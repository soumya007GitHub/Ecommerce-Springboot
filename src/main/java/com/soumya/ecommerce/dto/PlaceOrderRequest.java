package com.soumya.ecommerce.dto;

import com.soumya.ecommerce.entity.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceOrderRequest {

    @NotNull(message = "addressId is required")
    private UUID addressId;

    @NotNull(message = "paymentMethod is required")
    private PaymentMethod paymentMethod;
}
