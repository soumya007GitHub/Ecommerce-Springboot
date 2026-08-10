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

    private CategoryDTO mapToDTO(Category category) {

        List<CategoryTypeDTO> categoryTypes =
                category.getCategoryTypeList()
                        .stream()
                        .map(categoryType ->
                                CategoryTypeDTO.builder()
                                        .id(categoryType.getId())
                                        .name(categoryType.getName())
                                        .description(categoryType.getDescription())
                                        .build()
                        )
                        .toList();

        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .categoryTypes(categoryTypes)
                .build();
    }
}