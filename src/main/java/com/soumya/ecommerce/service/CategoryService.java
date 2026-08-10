package com.soumya.ecommerce.service;

import com.soumya.ecommerce.dto.CategoryDTO;
import com.soumya.ecommerce.dto.CategoryTypeDTO;
import com.soumya.ecommerce.entity.Category;
import com.soumya.ecommerce.entity.CategoryType;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    CategoryDTO getCategory(UUID categoryId);

    List<CategoryDTO> getCategories();

    CategoryDTO createCategory(CategoryDTO categoryDTO);

    CategoryDTO updateCategory(UUID categoryId, CategoryDTO categoryDTO);

    void deleteCategory(UUID categoryId);

    Category mapToEntity(CategoryDTO categoryDTO);

    CategoryDTO mapToDTO(Category category);

    List<CategoryType> mapToCategoryTypes(
            List<CategoryTypeDTO> categoryTypeDTOList,
            Category category
    );

    List<CategoryTypeDTO> mapToCategoryTypeDTOs(
            List<CategoryType> categoryTypes
    );
}