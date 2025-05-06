package com.kalavastra.api.service;

import com.kalavastra.api.exception.ResourceNotFoundException;
import com.kalavastra.api.model.Category;
import com.kalavastra.api.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service @RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository repo;

    @Transactional
    public Category create(Category c) {
        c.setCategoryCode(c.getName().toLowerCase().replaceAll("\\s+","-")
                          + "-" + UUID.randomUUID().toString().substring(0,6));
        return repo.save(c);
    }

    @Transactional(readOnly=true)
    public Category getByCode(String code) {
        return repo.findByCategoryCode(code)
                   .orElseThrow(() -> new ResourceNotFoundException("Category","categoryCode",code));
    }

    @Transactional
    public Category update(String code, Category req) {
        Category c = getByCode(code);
        c.setName(req.getName());
        c.setDescription(req.getDescription());
        return repo.save(c);
    }
    
    /** List all categories in the system */
    @Transactional(readOnly = true)
    public List<Category> getAllCategories() {
        return repo.findAll();
    }
}
