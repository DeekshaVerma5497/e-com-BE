package com.kalavastra.api.controller;

import com.kalavastra.api.model.Product;
import com.kalavastra.api.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<Product> create(@Valid @RequestBody Product product) {
        Product created = svc.create(product);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(created);
    }

    @Operation(summary="Get product by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable Long id) {
        return ResponseEntity.ok(svc.getById(id));
    }

    @Operation(summary="Get product by code")
    @GetMapping("/code/{code}")
    public ResponseEntity<Product> getByCode(
        @PathVariable("code") String code
    ) {
        return ResponseEntity.ok(svc.getByCode(code));
    }

    @Operation(summary="Update product by code")
    @PutMapping("/code/{code}")
    public ResponseEntity<Product> updateByCode(
        @PathVariable("code") String code,
        @Valid @RequestBody Product p
    ) {
        return ResponseEntity.ok(svc.updateByCode(code, p));
    }

    @Operation(summary="Delete product by productCode")
    @DeleteMapping("/code/{code}")
    public ResponseEntity<Void> deleteByCode(@PathVariable("code") String code) {
        svc.deleteByCode(code);
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
