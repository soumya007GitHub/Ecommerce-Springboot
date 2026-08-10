package com.soumya.ecommerce.service;

import com.soumya.ecommerce.dto.CategoryDTO;
import com.soumya.ecommerce.dto.CategoryTypeDTO;
import com.soumya.ecommerce.entity.Category;
import com.soumya.ecommerce.entity.CategoryType;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    CategoryDTO getCategory(UUID categoryId);

    CategoryDTO createCategory(CategoryDTO categoryDTO);

    Category mapToEntity(CategoryDTO categoryDTO);

    List<CategoryType> mapToCategoryTypes(
            List<CategoryTypeDTO> categoryTypeDTOList,
            Category category
    );
}