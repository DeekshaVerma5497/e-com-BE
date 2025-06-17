package com.kalavastra.api.service;

import com.kalavastra.api.exception.ResourceNotFoundException;
import com.kalavastra.api.model.*;
import com.kalavastra.api.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
	private final ProductRepository repo;
	private final CategoryService categoryService;
	private final ProductImageService imageService;

	@Transactional
	public Product create(Product product) {
		// resolve category
		Category cat = categoryService.getByCode(product.getCategoryCode());
		product.setCategory(cat);

		// auto‐slug productCode
		if (product.getProductCode() == null || product.getProductCode().isBlank()) {
			String slug = product.getName().trim().toLowerCase().replaceAll("\\s+", "-");
			product.setProductCode(slug + "-" + UUID.randomUUID().toString().substring(0, 6));
		}

		Product saved = repo.save(product);
		// images list will be empty until images are added
		saved.setImages(List.of());
		return saved;
	}

	@Transactional
	public Product updateByCode(String code, Product req) {
		Product existing = repo.findByProductCode(code)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "productCode", code));

		existing.setName(req.getName());
		existing.setDescription(req.getDescription());
		existing.setPrice(req.getPrice());
		existing.setStock(req.getStock());
		existing.setExtension(req.getExtension());
		existing.setIsActive(req.getIsActive());

		if (req.getCategoryCode() != null
				&& !req.getCategoryCode().equalsIgnoreCase(existing.getCategory().getCategoryCode())) {
			existing.setCategory(categoryService.getByCode(req.getCategoryCode()));
		}

		Product updated = repo.save(existing);
		// repopulate images
		List<String> urls = imageService.listImages(updated.getId()).stream().map(ProductImage::getImageUrl)
				.collect(Collectors.toList());
		updated.setImages(urls);

		return updated;
	}

	@Transactional(readOnly = true)
	public Product getById(Long id) {
		Product p = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product", "id", id.toString()));

		List<String> urls = imageService.listImages(id).stream().map(ProductImage::getImageUrl)
				.collect(Collectors.toList());
		enrichImages(p);
		return p;
	}

	private void enrichImages(Product p) {
		List<String> urls = imageService.listImages(p.getId()).stream().map(ProductImage::getImageUrl)
				.collect(Collectors.toList());
		p.setImages(urls);
		p.setImageUrl(urls.isEmpty() ? null : urls.get(0));
		// optional: pull originalPrice from JSON extension:
		Object op = p.getExtension().get("originalPrice");
		if (op instanceof Number) {
			p.setOriginalPrice(BigDecimal.valueOf(((Number) op).doubleValue()));
		}
	}

	@Transactional(readOnly = true)
	public Product getByCode(String code) {
		Product p = repo.findByProductCode(code)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "productCode", code));

		List<String> urls = imageService.listImages(p.getId()).stream().map(ProductImage::getImageUrl)
				.collect(Collectors.toList());
		p.setImages(urls);
		return p;
	}

	@Transactional
	public void deleteByCode(String code) {
		Product p = repo.findByProductCode(code)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "productCode", code));
		p.setIsActive(false);
		repo.save(p);
	}

	@Transactional(readOnly = true)
	public Page<Product> list(Map<String, String> filters, Pageable pg) {
		Specification<Product> spec = Specification.where(null);

		for (var e : filters.entrySet()) {
			spec = spec.and(dynamicFilter(e.getKey(), e.getValue()));
		}

		Page<Product> page = repo.findAll(spec, pg);
		page.getContent().forEach(p -> {
			List<String> urls = imageService.listImages(p.getId()).stream().map(ProductImage::getImageUrl)
					.collect(Collectors.toList());
			p.setImages(urls);
		});
		return page;
	}

	private Specification<Product> dynamicFilter(String key, String val) {
		return (root, query, cb) -> {
			if ("categoryCode".equalsIgnoreCase(key)) {
				Join<Product, Category> cat = root.join("category");
				return cb.equal(cat.get("categoryCode"), val);
			}
			try {
				Path<?> path = root.get(key);
				Class<?> t = path.getJavaType();
				Object v = switch (t.getSimpleName()) {
				case "Integer" -> Integer.valueOf(val);
				case "Long" -> Long.valueOf(val);
				case "BigDecimal" -> new BigDecimal(val);
				case "Boolean" -> Boolean.valueOf(val);
				default -> val;
				};
				return cb.equal(path, v);
			} catch (IllegalArgumentException ex) {
				Expression<String> jsonVal = cb.function("jsonb_extract_path_text", String.class, root.get("extension"),
						cb.literal(key));
				return cb.equal(jsonVal, val);
			}
		};
	}
}
