package com.kalavastra.api.controller;

import com.kalavastra.api.dto.CategoryDto;
import com.kalavastra.api.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Public endpoint for category listing and admin endpoint for adding new categories.
 */
@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "List all categories")
    @GetMapping
    public ResponseEntity<List<CategoryDto>> listAll() {
        return ResponseEntity.ok(categoryService.getAll());
    }

    @Operation(summary = "Create a new category (admin only)")
    @PostMapping
    public ResponseEntity<CategoryDto> create(@Valid @RequestBody CategoryDto dto) {
        return ResponseEntity.ok(categoryService.createCategory(dto));
    }
    
    @Operation(summary = "Update category by categoryCode")
    @PutMapping("/code/{categoryCode}")
    public ResponseEntity<CategoryDto> updateByCode(
        @PathVariable String categoryCode,
        @Valid @RequestBody CategoryDto dto
    ) {
      return ResponseEntity.ok(categoryService.updateByCode(categoryCode, dto));
    }
}
