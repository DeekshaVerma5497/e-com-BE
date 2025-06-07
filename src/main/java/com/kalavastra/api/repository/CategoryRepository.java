package com.kalavastra.api.repository;

import com.kalavastra.api.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
	Optional<Category> findByCategoryCode(String code);
}
