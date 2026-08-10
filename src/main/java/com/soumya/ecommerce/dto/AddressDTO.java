package com.soumya.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDTO {

    private UUID id;

    @NotBlank(message = "label is required")
    private String label;

    @NotBlank(message = "addressLine1 is required")
    private String addressLine1;

    private String addressLine2;

    @NotBlank(message = "city is required")
    private String city;

    @NotBlank(message = "state is required")
    private String state;

    @NotBlank(message = "postalCode is required")
    private String postalCode;

    @NotBlank(message = "country is required")
    private String country;

    @NotBlank(message = "contactPhone is required")
    private String contactPhone;

    private boolean isDefault;
}
