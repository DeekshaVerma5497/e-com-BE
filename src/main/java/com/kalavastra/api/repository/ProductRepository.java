package com.kalavastra.api.repository;

import com.kalavastra.api.model.Product;
import com.kalavastra.api.model.Category;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Repository for Product entity.
 */
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

	/**
	 * Fetch products with optional category filter and pagination.
	 */
	Page<Product> findAllByCategory(Category category, Pageable pageable);

	Optional<Product> findByProductCode(String productCode);

}
