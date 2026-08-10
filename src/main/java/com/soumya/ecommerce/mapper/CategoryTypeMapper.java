package com.soumya.ecommerce.mapper;

import com.soumya.ecommerce.dto.CategoryTypeDTO;
import com.soumya.ecommerce.entity.Category;
import com.soumya.ecommerce.entity.CategoryType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryTypeMapper {

    public CategoryType toEntity(CategoryTypeDTO dto, Category category) {

        CategoryType categoryType = new CategoryType();
        categoryType.setName(dto.getName());
        categoryType.setDescription(dto.getDescription());
        categoryType.setCategory(category);

        return categoryType;
    }

    public CategoryTypeDTO toDto(CategoryType categoryType) {

        return CategoryTypeDTO.builder()
                .id(categoryType.getId())
                .name(categoryType.getName())
                .description(categoryType.getDescription())
                .categoryId(categoryType.getCategory() != null ? categoryType.getCategory().getId() : null)
                .build();
    }

    public List<CategoryTypeDTO> toDtoList(List<CategoryType> categoryTypes) {

        if (categoryTypes == null) {
            return List.of();
        }

        return categoryTypes.stream()
                .map(this::toDto)
                .toList();
    }
}
