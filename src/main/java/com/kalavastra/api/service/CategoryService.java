package com.kalavastra.api.service;

import com.kalavastra.api.dto.CategoryDto;
import com.kalavastra.api.exception.ResourceNotFoundException;
import com.kalavastra.api.mapper.DomainMapper;
import com.kalavastra.api.model.Category;
import com.kalavastra.api.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles business logic for category creation and listing.
 */
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepo;
    private final DomainMapper mapper;
    
    public CategoryDto createCategory(CategoryDto dto) {
        // Auto-generate code if not present
        String code = (dto.getCategoryCode() != null && !dto.getCategoryCode().isBlank())
            ? dto.getCategoryCode().toLowerCase()
            : dto.getName().toLowerCase().replaceAll("\\s+", "_");

        Category category = Category.builder()
            .name(dto.getName())
            .description(dto.getDescription())
            .categoryCode(code)
            .build();

        return mapper.categoryToDto(categoryRepo.save(category));
    }


    public List<CategoryDto> getAll() {
        return categoryRepo.findAll()
                .stream()
                .map(mapper::categoryToDto)
                .collect(Collectors.toList());
    }

    public Category getByCode(String categoryCode) {
        return categoryRepo.findByCategoryCode(categoryCode)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryCode", categoryCode));
    }
    
    /**
     * Update an existing category looked up by its business key (categoryCode).
     */
    public CategoryDto updateByCode(String categoryCode, CategoryDto dto) {
        Category category = categoryRepo.findByCategoryCode(categoryCode)
          .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryCode", categoryCode));

        // this will copy only name/description (and any other non‑null fields) —
        // it will *not* overwrite categoryCode, thanks to our @Mapping above
        mapper.updateCategoryFromDto(dto, category);

        // if the user really wants to change the code, do it explicitly:
        if (dto.getCategoryCode() != null && !dto.getCategoryCode().isBlank()) {
          category.setCategoryCode(dto.getCategoryCode().toLowerCase());
        }

        return mapper.categoryToDto(categoryRepo.save(category));
      }
}
