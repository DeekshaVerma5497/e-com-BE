package com.kalavastra.api.repository;

import com.kalavastra.api.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for Category entity.
 * Provides basic CRUD operations and custom finder by categoryCode.
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Find a category using its business-level code.
     */
    Optional<Category> findByCategoryCode(String categoryCode);
}
