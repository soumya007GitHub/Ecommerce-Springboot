package com.soumya.ecommerce.service;

import com.soumya.ecommerce.dto.CategoryTypeDTO;
import com.soumya.ecommerce.entity.Category;
import com.soumya.ecommerce.entity.CategoryType;
import com.soumya.ecommerce.exception.ResourceNotFoundException;
import com.soumya.ecommerce.mapper.CategoryTypeMapper;
import com.soumya.ecommerce.repository.CategoryRepository;
import com.soumya.ecommerce.repository.CategoryTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryTypeServiceImpl implements CategoryTypeService {

    private final CategoryTypeRepository categoryTypeRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryTypeMapper categoryTypeMapper;

    @Override
    @Transactional(readOnly = true)
    public CategoryTypeDTO getCategoryType(UUID categoryTypeId) {

        CategoryType categoryType = categoryTypeRepository.findById(categoryTypeId)
                .orElseThrow(() -> ResourceNotFoundException.of("CategoryType", categoryTypeId));

        return categoryTypeMapper.toDto(categoryType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryTypeDTO> getCategoryTypesByCategory(UUID categoryId) {

        return categoryTypeRepository.findByCategoryId(categoryId).stream()
                .map(categoryTypeMapper::toDto)
                .toList();
    }

    @Override
    public CategoryTypeDTO createCategoryType(CategoryTypeDTO categoryTypeDTO) {

        Category category = categoryRepository.findById(categoryTypeDTO.getCategoryId())
                .orElseThrow(() -> ResourceNotFoundException.of("Category", categoryTypeDTO.getCategoryId()));

        CategoryType categoryType = categoryTypeMapper.toEntity(categoryTypeDTO, category);

        CategoryType savedCategoryType = categoryTypeRepository.save(categoryType);

        return categoryTypeMapper.toDto(savedCategoryType);
    }

    @Override
    public CategoryTypeDTO updateCategoryType(UUID categoryTypeId, CategoryTypeDTO categoryTypeDTO) {

        CategoryType categoryType = categoryTypeRepository.findById(categoryTypeId)
                .orElseThrow(() -> ResourceNotFoundException.of("CategoryType", categoryTypeId));

        Category category = categoryRepository.findById(categoryTypeDTO.getCategoryId())
                .orElseThrow(() -> ResourceNotFoundException.of("Category", categoryTypeDTO.getCategoryId()));

        categoryType.setName(categoryTypeDTO.getName());
        categoryType.setDescription(categoryTypeDTO.getDescription());
        categoryType.setCategory(category);

        CategoryType updatedCategoryType = categoryTypeRepository.save(categoryType);

        return categoryTypeMapper.toDto(updatedCategoryType);
    }

    @Override
    public void deleteCategoryType(UUID categoryTypeId) {

        CategoryType categoryType = categoryTypeRepository.findById(categoryTypeId)
                .orElseThrow(() -> ResourceNotFoundException.of("CategoryType", categoryTypeId));

        categoryTypeRepository.delete(categoryType);
    }
}
