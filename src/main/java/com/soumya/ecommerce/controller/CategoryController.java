package com.soumya.ecommerce.controller;

import com.soumya.ecommerce.dto.CategoryDTO;
import com.soumya.ecommerce.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDTO> getCategoryById(
            @PathVariable UUID categoryId
    ) {

        CategoryDTO categoryDTO =
                categoryService.getCategory(categoryId);

        return ResponseEntity.ok(categoryDTO);
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(
            @RequestBody CategoryDTO categoryDTO
    ) {

        CategoryDTO createdCategory =
                categoryService.createCategory(categoryDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdCategory);
    }
}