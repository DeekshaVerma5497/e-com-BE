package com.kalavastra.api.service;

import com.kalavastra.api.exception.ResourceNotFoundException;
import com.kalavastra.api.model.Category;
import com.kalavastra.api.model.Product;
import com.kalavastra.api.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

@Service @RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repo;
    private final CategoryService categoryService;

    @Transactional
    public Product create(Product product) {
        // 1. Resolve Category by the transient categoryCode
        String code = product.getCategoryCode();
        Category cat = categoryService.getByCode(code);
        product.setCategory(cat);

        // 2. Generate a slug-style productCode if none was provided
        if (product.getProductCode() == null || product.getProductCode().isBlank()) {
            String slug = product.getName()
                                 .trim()
                                 .toLowerCase()
                                 .replaceAll("\\s+", "-");
            String random = UUID.randomUUID().toString().substring(0, 6);
            product.setProductCode(slug + "-" + random);
        }

        // 3. Persist
        return repo.save(product);
    }

    @Transactional
    public Product updateByCode(String productCode, Product req) {
        Product existing = repo.findByProductCode(productCode)
            .orElseThrow(() ->
                new ResourceNotFoundException("Product", "productCode", productCode)
            );

        // copy simple fields
        existing.setName(req.getName());
        existing.setDescription(req.getDescription());
        existing.setPrice(req.getPrice());
        existing.setStock(req.getStock());
        existing.setExtension(req.getExtension());
        existing.setIsActive(req.getIsActive());

        // update category if a new code was passed
        String newCode = req.getCategoryCode();
        if (newCode != null &&
            !newCode.equalsIgnoreCase(existing.getCategory().getCategoryCode())) {
            existing.setCategory(categoryService.getByCode(newCode));
        }

        return repo.save(existing);
    }

    @Transactional(readOnly = true)
    public Product getByCode(String productCode) {
        return repo.findByProductCode(productCode)
            .orElseThrow(() ->
                new ResourceNotFoundException("Product", "productCode", productCode)
            );
    }

    @Transactional(readOnly=true)
    public Product getById(Long id) {
        return repo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product","id",id.toString()));
    }

    @Transactional
    public void deleteByCode(String productCode) {
        Product p = repo.findByProductCode(productCode)
            .orElseThrow(() ->
                new ResourceNotFoundException("Product", "productCode", productCode)
            );

        // mark inactive and save
        p.setIsActive(false);
        repo.save(p);
    }

    @Transactional(readOnly=true)
    public Page<Product> list(String categoryCode, Pageable pg) {
        if (categoryCode!=null) {
            Category cat = categoryService.getByCode(categoryCode);
            return repo.findAllByCategory(cat, pg);
        }
        return repo.findAll(pg);
    }
}
