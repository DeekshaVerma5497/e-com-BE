package com.kalavastra.api.controller;

import com.kalavastra.api.auth.AuthService;
import com.kalavastra.api.model.Product;
import com.kalavastra.api.model.WishlistItem;
import com.kalavastra.api.service.ProductService;
import com.kalavastra.api.service.WishlistService;

import io.swagger.v3.oas.annotations.Operation;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
	private final ProductService svc;
	private final WishlistService wishlistService;
	private final AuthService authService;

	@Operation(summary = "Create new product")
	@PostMapping
	public ResponseEntity<Product> create(@Valid @RequestBody Product product) {
		Product created = svc.create(product);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	@Operation(summary = "Get product by ID (with optional wishlisted flag)")
    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable("id") Long id) {
        Product p = svc.getById(id);

        // only try to fetch wishlisted if the user is authenticated
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null
            && auth.isAuthenticated()
            && !(auth instanceof AnonymousAuthenticationToken)) {
            String uid = authService.getCurrentUser().getUserId();
            boolean wished = wishlistService.isWishlisted(uid, id);
            p.setWishlisted(wished);
        }

        return ResponseEntity.ok(p);
    }

	@Operation(summary = "Get product by code")
	@GetMapping("/code/{code}")
	public ResponseEntity<Product> getByCode(@PathVariable("code") String code) {
		return ResponseEntity.ok(svc.getByCode(code));
	}

	@Operation(summary = "Update product by code")
	@PutMapping("/code/{code}")
	public ResponseEntity<Product> updateByCode(@PathVariable("code") String code,
			@Valid @RequestBody Product product) {
		return ResponseEntity.ok(svc.updateByCode(code, product));
	}

	@Operation(summary = "Soft-delete product by code")
	@DeleteMapping("/code/{code}")
	public ResponseEntity<Void> deleteByCode(@PathVariable("code") String code) {
		svc.deleteByCode(code);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "List products (with optional wishlisted flags)")
    @GetMapping
    public ResponseEntity<Page<Product>> list(
            @RequestParam Map<String, String> allParams,
            Pageable pageable) {

        // strip paging/sort params
        Map<String,String> filters = new HashMap<>(allParams);
        filters.keySet().removeAll(Arrays.asList("page", "size", "sort"));

        Page<Product> page = svc.list(filters, pageable);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null
            && auth.isAuthenticated()
            && !(auth instanceof AnonymousAuthenticationToken)) {

            String uid = authService.getCurrentUser().getUserId();
            List<WishlistItem> items = wishlistService.listItems(uid);
            Set<Long> wishedIds = items.stream()
                                       .filter(WishlistItem::getIsActive)
                                       .map(wi -> wi.getProduct().getId())
                                       .collect(Collectors.toSet());

            page.getContent().forEach(p -> p.setWishlisted(wishedIds.contains(p.getId())));
        }

        return ResponseEntity.ok(page);
    }
}
