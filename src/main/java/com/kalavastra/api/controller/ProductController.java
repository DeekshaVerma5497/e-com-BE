package com.kalavastra.api.controller;

import com.kalavastra.api.model.Product;
import com.kalavastra.api.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService svc;

    @Operation(summary="Create new product")
    @PostMapping
    public ResponseEntity<Product> create(@Valid @RequestBody Product p) {
        return ResponseEntity.ok(svc.create(p));
    }

    @Operation(summary="Update product")
    @PutMapping("/{id}")
    public ResponseEntity<Product> update(
        @PathVariable Long id,
        @Valid @RequestBody Product p
    ) {
        return ResponseEntity.ok(svc.update(id, p));
    }

    @Operation(summary="Get product by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable Long id) {
        return ResponseEntity.ok(svc.getById(id));
    }

    @Operation(summary="Get product by code")
    @GetMapping("/code/{code}")
    public ResponseEntity<Product> getByCode(@PathVariable String code) {
        return ResponseEntity.ok(svc.getByCode(code));
    }

    @Operation(summary="Delete product")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        svc.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary="List products")
    @GetMapping
    public ResponseEntity<Page<Product>> list(
        @RequestParam(required=false) String categoryCode,
        Pageable pageable
    ) {
        return ResponseEntity.ok(svc.list(categoryCode, pageable));
    }
}
