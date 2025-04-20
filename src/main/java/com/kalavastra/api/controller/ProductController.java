package com.kalavastra.api.controller;

import com.kalavastra.api.dto.ProductRequestDto;
import com.kalavastra.api.dto.ProductResponseDto;
import com.kalavastra.api.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Public product catalog with pagination and admin create/update/delete endpoints.
 */
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Create new product (admin only)")
    @PostMapping
    public ResponseEntity<ProductResponseDto> create(@Valid @RequestBody ProductRequestDto dto) {
        return ResponseEntity.ok(productService.create(dto));
    }

    @Operation(summary = "Update product by ID")
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequestDto dto
    ) {
        return ResponseEntity.ok(productService.update(id, dto));
    }

    @Operation(summary = "Get product by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    @Operation(summary = "Delete product by ID (admin only)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/code/{productCode}")
    @Operation(summary = "Update product by product code")
    public ResponseEntity<ProductResponseDto> updateByCode(
            @PathVariable String productCode,
            @Valid @RequestBody ProductRequestDto dto
    ) {
        return ResponseEntity.ok(productService.updateByCode(productCode, dto));
    }
    
    @Operation(summary = "Get product by productCode")
    @GetMapping("/code/{productCode}")
    public ResponseEntity<ProductResponseDto> getByCode(
            @PathVariable String productCode
    ) {
        return ResponseEntity.ok(productService.getByCode(productCode));
    }


    @Operation(summary = "List products with optional category filter")
    @GetMapping
    public ResponseEntity<Page<ProductResponseDto>> list(
            @RequestParam(required = false) String categoryCode,
            Pageable pageable
    ) {
        return ResponseEntity.ok(productService.getAll(categoryCode, pageable));
    }
}
