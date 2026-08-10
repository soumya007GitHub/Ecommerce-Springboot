package com.soumya.ecommerce.mapper;

import com.soumya.ecommerce.dto.CategoryDTO;
import com.soumya.ecommerce.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryMapper {

    private final CategoryTypeMapper categoryTypeMapper;

    public Category toEntity(CategoryDTO dto) {

        Category category = new Category();
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());

        return category;
    }

    public CategoryDTO toDto(Category category) {

        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .categoryTypes(categoryTypeMapper.toDtoList(category.getCategoryTypeList()))
                .build();
    }
}
