package com.soumya.ecommerce.controller;

import com.soumya.ecommerce.dto.CategoryTypeDTO;
import com.soumya.ecommerce.service.CategoryTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/category-types")
@RequiredArgsConstructor
public class CategoryTypeController {

    private final CategoryTypeService categoryTypeService;

    @GetMapping("/{categoryTypeId}")
    public ResponseEntity<CategoryTypeDTO> getCategoryType(@PathVariable UUID categoryTypeId) {
        return ResponseEntity.ok(categoryTypeService.getCategoryType(categoryTypeId));
    }

    @GetMapping
    public ResponseEntity<List<CategoryTypeDTO>> getCategoryTypesByCategory(@RequestParam UUID categoryId) {
        return ResponseEntity.ok(categoryTypeService.getCategoryTypesByCategory(categoryId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CategoryTypeDTO> createCategoryType(@Valid @RequestBody CategoryTypeDTO categoryTypeDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryTypeService.createCategoryType(categoryTypeDTO));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{categoryTypeId}")
    public ResponseEntity<CategoryTypeDTO> updateCategoryType(
            @PathVariable UUID categoryTypeId,
            @Valid @RequestBody CategoryTypeDTO categoryTypeDTO
    ) {
        return ResponseEntity.ok(categoryTypeService.updateCategoryType(categoryTypeId, categoryTypeDTO));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{categoryTypeId}")
    public ResponseEntity<Void> deleteCategoryType(@PathVariable UUID categoryTypeId) {
        categoryTypeService.deleteCategoryType(categoryTypeId);
        return ResponseEntity.noContent().build();
    }
}
