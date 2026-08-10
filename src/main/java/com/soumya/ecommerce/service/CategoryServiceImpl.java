package com.soumya.ecommerce.service;

import com.soumya.ecommerce.dto.CategoryDTO;
import com.soumya.ecommerce.dto.CategoryTypeDTO;
import com.soumya.ecommerce.entity.Category;
import com.soumya.ecommerce.entity.CategoryType;
import com.soumya.ecommerce.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;


    @Override
    public List<CategoryDTO> getCategories() {

        List<Category> categories = categoryRepository.findAll();

        return categories.stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public CategoryDTO getCategory(UUID categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new RuntimeException("Category not found with id: " + categoryId)
                );

        return mapToDTO(category);
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {

        Category category = mapToEntity(categoryDTO);

        Category savedCategory = categoryRepository.save(category);

        return mapToDTO(savedCategory);
    }

    @Override
    public CategoryDTO updateCategory(UUID categoryId, CategoryDTO categoryDTO) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new RuntimeException("Category not found with id: " + categoryId)
                );

        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());

        List<CategoryType> categoryTypes =
                mapToCategoryTypes(
                        categoryDTO.getCategoryTypes(),
                        category
                );

        category.setCategoryTypeList(categoryTypes);

        Category updatedCategory = categoryRepository.save(category);

        return mapToDTO(updatedCategory);
    }

    @Override
    public Category mapToEntity(CategoryDTO categoryDTO) {

        Category category = new Category();

        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());

        List<CategoryType> categoryTypes =
                mapToCategoryTypes(
                        categoryDTO.getCategoryTypes(),
                        category
                );

        category.setCategoryTypeList(categoryTypes);

        return category;
    }

    @Override
    public void deleteCategory(UUID categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new RuntimeException("Category not found with id: " + categoryId)
                );

        categoryRepository.delete(category);
    }

    @Override
    public List<CategoryType> mapToCategoryTypes(
            List<CategoryTypeDTO> categoryTypeDTOList,
            Category category
    ) {

        if (categoryTypeDTOList == null) {
            return List.of();
        }

        return categoryTypeDTOList.stream()
                .map(categoryTypeDTO -> {

                    CategoryType categoryType = new CategoryType();

                    categoryType.setName(categoryTypeDTO.getName());
                    categoryType.setDescription(
                            categoryTypeDTO.getDescription()
                    );

                    categoryType.setCategory(category);

                    return categoryType;

                })
                .toList();
    }

    @Override
    public CategoryDTO mapToDTO(Category category) {

        List<CategoryTypeDTO> categoryTypes =
                mapToCategoryTypeDTOs(category.getCategoryTypeList());

        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .categoryTypes(categoryTypes)
                .build();
    }

    @Override
    public List<CategoryTypeDTO> mapToCategoryTypeDTOs(
            List<CategoryType> categoryTypes
    ) {

        if (categoryTypes == null) {
            return List.of();
        }

        return categoryTypes.stream()
                .map(categoryType -> CategoryTypeDTO.builder()
                        .id(categoryType.getId())
                        .name(categoryType.getName())
                        .description(categoryType.getDescription())
                        .build())
                .toList();
    }
}