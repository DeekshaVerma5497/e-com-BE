package com.kalavastra.api.controller;

import com.kalavastra.api.model.Product;
import com.kalavastra.api.model.ProductImage;
import com.kalavastra.api.service.ProductImageService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products/{productId}/images")
public class ProductImageController {

    private final ProductImageService service;

    public ProductImageController(ProductImageService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ProductImage> addImage(
            @PathVariable Long productId, @Valid @RequestBody ProductImage productImage) {
        ProductImage img = service.addImage(productId, productImage);
        return ResponseEntity.ok(img);
    }

    @GetMapping
    public ResponseEntity<List<ProductImage>> list(
            @PathVariable Long productId) {
        return ResponseEntity.ok(service.listImages(productId));
    }

    @DeleteMapping("/{imageId}")
    public ResponseEntity<Void> remove(
            @PathVariable Long productId,
            @PathVariable Long imageId) {
        service.softDelete(imageId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{imageId}/primary")
    public ResponseEntity<ProductImage> markPrimary(
            @PathVariable Long productId,
            @PathVariable Long imageId) {
        return ResponseEntity.ok(service.markPrimary(imageId));
    }
}
