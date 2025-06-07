package com.kalavastra.api.service;

import com.kalavastra.api.exception.ResourceNotFoundException;
import com.kalavastra.api.model.Category;
import com.kalavastra.api.model.Product;
import com.kalavastra.api.model.ProductImage;
import com.kalavastra.api.repository.ProductRepository;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
	private final ProductRepository repo;
	private final CategoryService categoryService;
	private final ProductImageService imageService;

	@Transactional
	public Product create(Product product) {
		// 1. Resolve Category by the transient categoryCode
		String code = product.getCategoryCode();
		Category cat = categoryService.getByCode(code);
		product.setCategory(cat);

		// 2. Generate a slug-style productCode if none was provided
		if (product.getProductCode() == null || product.getProductCode().isBlank()) {
			String slug = product.getName().trim().toLowerCase().replaceAll("\\s+", "-");
			String random = UUID.randomUUID().toString().substring(0, 6);
			product.setProductCode(slug + "-" + random);
		}

		// 3. Persist
		return repo.save(product);
	}

	@Transactional
	public Product updateByCode(String productCode, Product req) {
		Product existing = repo.findByProductCode(productCode)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "productCode", productCode));

		// copy simple fields
		existing.setName(req.getName());
		existing.setDescription(req.getDescription());
		existing.setPrice(req.getPrice());
		existing.setStock(req.getStock());
		existing.setExtension(req.getExtension());
		existing.setIsActive(req.getIsActive());

		// update category if a new code was passed
		String newCode = req.getCategoryCode();
		if (newCode != null && !newCode.equalsIgnoreCase(existing.getCategory().getCategoryCode())) {
			existing.setCategory(categoryService.getByCode(newCode));
		}

		return repo.save(existing);
	}

	@Transactional(readOnly = true)
	public Product getByCode(String productCode) {
		return repo.findByProductCode(productCode)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "productCode", productCode));
	}

	@Transactional(readOnly = true)
	public Product getById(Long id) {
		return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product", "id", id.toString()));
	}

	@Transactional
	public void deleteByCode(String productCode) {
		Product p = repo.findByProductCode(productCode)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "productCode", productCode));

		// mark inactive and save
		p.setIsActive(false);
		repo.save(p);
	}

	@Transactional(readOnly = true)
	public Page<Product> list(Map<String, String> filters, Pageable pg) {
		Specification<Product> spec = Specification.where(null);

		for (Map.Entry<String, String> entry : filters.entrySet()) {
			String key = entry.getKey();
			String val = entry.getValue();

			spec = spec.and(dynamicFilter(key, val));
		}

		Page<Product> page = repo.findAll(spec, pg);

		// Populate images for each product
		page.getContent().forEach(prod -> {
			List<ProductImage> imgs = imageService.listImages(prod.getId());
			List<String> urls = imgs.stream().map(ProductImage::getImageUrl).collect(Collectors.toList());
			prod.setImages(urls);
		});

		return page;
	}

	// inside ProductService

	private Specification<Product> dynamicFilter(String key, String val) {
		return (root, query, cb) -> {
			// ───────────────────────────────────────────────
			// Special‐case categoryCode → join on Category
			// ───────────────────────────────────────────────
			if ("categoryCode".equalsIgnoreCase(key)) {
				// join to Category entity
				var catJoin = root.join("category");
				return cb.equal(catJoin.get("categoryCode"), val);
			}

			// ───────────────────────────────────────────────
			// Otherwise, try a real column on Product
			// ───────────────────────────────────────────────
			try {
				Path<?> path = root.get(key);
				Class<?> javaType = path.getJavaType();
				Object typedValue;
				if (javaType.equals(Integer.class))
					typedValue = Integer.valueOf(val);
				else if (javaType.equals(Long.class))
					typedValue = Long.valueOf(val);
				else if (javaType.equals(BigDecimal.class))
					typedValue = new BigDecimal(val);
				else if (javaType.equals(Boolean.class))
					typedValue = Boolean.valueOf(val);
				else
					typedValue = val;
				return cb.equal(path, typedValue);

			} catch (IllegalArgumentException ex) {
				// ───────────────────────────────────────────────
				// Fallback: JSONB extension (only for truly dynamic keys)
				// ───────────────────────────────────────────────
				Expression<String> jsonVal = cb.function("jsonb_extract_path_text", String.class, root.get("extension"),
						cb.literal(key));
				return cb.equal(jsonVal, val);
			}
		};
	}

}
