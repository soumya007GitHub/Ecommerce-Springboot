package com.soumya.ecommerce.repository;

import com.soumya.ecommerce.entity.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CategoryTypeRepository extends JpaRepository<CategoryType, UUID> {

    List<CategoryType> findByCategoryId(UUID categoryId);
}
