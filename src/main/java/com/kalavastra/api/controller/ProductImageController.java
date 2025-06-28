package com.kalavastra.api.controller;

import com.kalavastra.api.model.ProductImage;
import com.kalavastra.api.service.ProductImageService;
import com.kalavastra.api.service.FileStorageService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products/{productId}/images")
@RequiredArgsConstructor
public class ProductImageController {

	private final ProductImageService service;
	private final FileStorageService fileStorage;

	@Operation(summary = "Add image record via JSON")
	@PostMapping
	public ResponseEntity<ProductImage> addImage(@PathVariable("productId") Long productId,
			@Valid @RequestBody ProductImage productImage) {
		ProductImage img = service.addImage(productId, productImage);
		return ResponseEntity.ok(img);
	}

	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ProductImage> uploadImage(@PathVariable("productId") Long productId,
			@RequestParam("file") MultipartFile file) throws IOException {
		// 1) copy file & get its public URL
		String url = fileStorage.saveProductImage(file);

		// 2) persist the DB record
		ProductImage img = service.addImage(productId, ProductImage.builder().imageUrl(url).isPrimary(false).build());
		return ResponseEntity.ok(img);
	}

	@Operation(summary = "List active images")
	@GetMapping
	public ResponseEntity<List<ProductImage>> list(@PathVariable("productId") Long productId) {
		return ResponseEntity.ok(service.listImages(productId));
	}

	@Operation(summary = "Soft-delete one image")
	@DeleteMapping("/{imageId}")
	public ResponseEntity<Void> remove(@PathVariable("productId") Long productId,
			@PathVariable("imageId") Long imageId) {
		service.softDelete(imageId);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "Mark one image as primary")
	@PutMapping("/{imageId}/primary")
	public ResponseEntity<ProductImage> markPrimary(@PathVariable("productId") Long productId,
			@PathVariable("imageId") Long imageId) {
		return ResponseEntity.ok(service.markPrimary(imageId));
	}
}
