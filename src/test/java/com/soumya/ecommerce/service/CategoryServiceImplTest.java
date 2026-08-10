package com.soumya.ecommerce.service;

import com.soumya.ecommerce.dto.CategoryDTO;
import com.soumya.ecommerce.entity.Category;
import com.soumya.ecommerce.exception.DuplicateResourceException;
import com.soumya.ecommerce.exception.ResourceNotFoundException;
import com.soumya.ecommerce.mapper.CategoryMapper;
import com.soumya.ecommerce.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void createCategory_throwsWhenNameAlreadyExists() {

        CategoryDTO dto = CategoryDTO.builder().name("Footwear").build();

        when(categoryRepository.existsByNameIgnoreCase("Footwear")).thenReturn(true);

        assertThatThrownBy(() -> categoryService.createCategory(dto))
                .isInstanceOf(DuplicateResourceException.class);
    }

    @Test
    void createCategory_savesWhenNameIsUnique() {

        CategoryDTO dto = CategoryDTO.builder().name("Footwear").build();
        Category entity = new Category();
        Category saved = new Category();
        saved.setId(UUID.randomUUID());

        when(categoryRepository.existsByNameIgnoreCase("Footwear")).thenReturn(false);
        when(categoryMapper.toEntity(dto)).thenReturn(entity);
        when(categoryRepository.save(entity)).thenReturn(saved);
        when(categoryMapper.toDto(saved)).thenReturn(CategoryDTO.builder().id(saved.getId()).name("Footwear").build());

        CategoryDTO result = categoryService.createCategory(dto);

        assertThat(result.getId()).isEqualTo(saved.getId());
    }

    @Test
    void getCategory_throwsWhenNotFound() {

        UUID categoryId = UUID.randomUUID();

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.getCategory(categoryId))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
