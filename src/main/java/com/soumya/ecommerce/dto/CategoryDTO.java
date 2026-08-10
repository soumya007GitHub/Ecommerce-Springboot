package com.soumya.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDTO {

    private UUID id;

    @NotBlank(message = "name is required")
    private String name;

    private String description;

    private List<CategoryTypeDTO> categoryTypes;
}
