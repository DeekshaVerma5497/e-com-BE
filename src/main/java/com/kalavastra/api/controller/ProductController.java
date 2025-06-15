package com.kalavastra.api.controller;

import com.kalavastra.api.model.Product;
import com.kalavastra.api.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService svc;

    @Operation(summary = "Create new product")
    @PostMapping
    public ResponseEntity<Product> create(@Valid @RequestBody Product product) {
        Product created = svc.create(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Get product by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(svc.getById(id));
    }

    @Operation(summary = "Get product by code")
    @GetMapping("/code/{code}")
    public ResponseEntity<Product> getByCode(@PathVariable("code") String code) {
        return ResponseEntity.ok(svc.getByCode(code));
    }

    @Operation(summary = "Update product by code")
    @PutMapping("/code/{code}")
    public ResponseEntity<Product> updateByCode(
        @PathVariable("code") String code,
        @Valid @RequestBody Product product
    ) {
        return ResponseEntity.ok(svc.updateByCode(code, product));
    }

    @Operation(summary = "Soft-delete product by code")
    @DeleteMapping("/code/{code}")
    public ResponseEntity<Void> deleteByCode(@PathVariable("code") String code) {
        svc.deleteByCode(code);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<Product>> list(
        @RequestParam Map<String,String> allParams,
        Pageable pageable
    ) {
        var filters = new HashMap<>(allParams);
        filters.remove("page");
        filters.remove("size");
        filters.remove("sort");
        return ResponseEntity.ok(svc.list(filters, pageable));
    }
}
