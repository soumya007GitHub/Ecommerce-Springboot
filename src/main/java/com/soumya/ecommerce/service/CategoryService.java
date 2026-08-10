package com.soumya.ecommerce.service;

import com.soumya.ecommerce.dto.CategoryDTO;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    CategoryDTO getCategory(UUID categoryId);

    List<CategoryDTO> getCategories();

    CategoryDTO createCategory(CategoryDTO categoryDTO);

    CategoryDTO updateCategory(UUID categoryId, CategoryDTO categoryDTO);

    void deleteCategory(UUID categoryId);
}
