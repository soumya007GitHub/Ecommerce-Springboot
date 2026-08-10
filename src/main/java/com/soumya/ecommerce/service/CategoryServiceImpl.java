package com.soumya.ecommerce.service;

import com.soumya.ecommerce.dto.CategoryDTO;
import com.soumya.ecommerce.entity.Category;
import com.soumya.ecommerce.exception.DuplicateResourceException;
import com.soumya.ecommerce.exception.ResourceNotFoundException;
import com.soumya.ecommerce.mapper.CategoryMapper;
import com.soumya.ecommerce.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDTO> getCategories() {

        return categoryRepository.findAll().stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDTO getCategory(UUID categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> ResourceNotFoundException.of("Category", categoryId));

        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {

        if (categoryRepository.existsByNameIgnoreCase(categoryDTO.getName())) {
            throw new DuplicateResourceException("Category already exists with name: " + categoryDTO.getName());
        }

        Category category = categoryMapper.toEntity(categoryDTO);

        Category savedCategory = categoryRepository.save(category);

        return categoryMapper.toDto(savedCategory);
    }

    @Override
    public CategoryDTO updateCategory(UUID categoryId, CategoryDTO categoryDTO) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> ResourceNotFoundException.of("Category", categoryId));

        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());

        Category updatedCategory = categoryRepository.save(category);

        return categoryMapper.toDto(updatedCategory);
    }

    @Override
    public void deleteCategory(UUID categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> ResourceNotFoundException.of("Category", categoryId));

        categoryRepository.delete(category);
    }
}
