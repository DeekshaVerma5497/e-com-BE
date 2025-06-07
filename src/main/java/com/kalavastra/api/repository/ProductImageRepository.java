package com.kalavastra.api.repository;

import com.kalavastra.api.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
	List<ProductImage> findAllByProductIdAndIsActiveTrue(Long productId);
}
