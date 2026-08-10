package com.soumya.ecommerce.service;

import com.soumya.ecommerce.dto.CategoryTypeDTO;

import java.util.List;
import java.util.UUID;

public interface CategoryTypeService {

    CategoryTypeDTO getCategoryType(UUID categoryTypeId);

    List<CategoryTypeDTO> getCategoryTypesByCategory(UUID categoryId);

    CategoryTypeDTO createCategoryType(CategoryTypeDTO categoryTypeDTO);

    CategoryTypeDTO updateCategoryType(UUID categoryTypeId, CategoryTypeDTO categoryTypeDTO);

    void deleteCategoryType(UUID categoryTypeId);
}
