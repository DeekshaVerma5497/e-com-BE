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
    public Product create(Product p) {
        Category cat = categoryService.getByCode(p.getCategory().getCategoryCode());
        p.setCategory(cat);
        if (p.getProductCode()==null || p.getProductCode().isBlank())
            p.setProductCode(p.getName().toLowerCase().replaceAll("\\s+","-")
                               + "-" + UUID.randomUUID().toString().substring(0,6));
        return repo.save(p);
    }

    @Transactional
    public Product update(Long id, Product req) {
        Product p = repo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product","id",id.toString()));
        p.setName(req.getName());
        p.setDescription(req.getDescription());
        p.setPrice(req.getPrice());
        p.setStock(req.getStock());
        // update category if changed
        if (!p.getCategory().getCategoryCode()
               .equalsIgnoreCase(req.getCategory().getCategoryCode())) {
            p.setCategory(categoryService.getByCode(req.getCategory().getCategoryCode()));
        }
        return repo.save(p);
    }

    @Transactional(readOnly=true)
    public Product getById(Long id) {
        return repo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product","id",id.toString()));
    }

    @Transactional(readOnly=true)
    public Product getByCode(String code) {
        return repo.findByProductCode(code)
            .orElseThrow(() -> new ResourceNotFoundException("Product","productCode",code));
    }

    @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("Product","id",id.toString());
        repo.deleteById(id);
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
