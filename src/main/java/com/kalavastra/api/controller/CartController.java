package com.kalavastra.api.controller;

import com.kalavastra.api.dto.*;
import com.kalavastra.api.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Secured endpoints for shopping‑cart operations.
 */
@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @Operation(summary = "1. Create or get default cart for a user")
    @PostMapping("/{userId}")
    public ResponseEntity<CartResponseDto> createCart(@PathVariable String userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @Operation(summary = "2. Get cart by user")
    @GetMapping("/{userId}")
    public ResponseEntity<CartResponseDto> getCart(@PathVariable String userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @Operation(summary = "3. Add item to cart")
    @PostMapping("/{userId}/items")
    public ResponseEntity<CartResponseDto> addItem(
            @PathVariable String userId,
            @Valid @RequestBody CartItemRequestDto req
    ) {
        return ResponseEntity.ok(cartService.addItem(userId, req));
    }

    @Operation(summary = "6. Update an item’s quantity")
    @PutMapping("/{userId}/items/{itemId}")
    public ResponseEntity<CartResponseDto> updateItem(
            @PathVariable String userId,
            @PathVariable Long itemId,
            @Valid @RequestBody CartItemRequestDto req
    ) {
        return ResponseEntity.ok(cartService.updateItem(userId, itemId, req));
    }

    @Operation(summary = "4. Remove a single item")
    @DeleteMapping("/{userId}/items/{itemId}")
    public ResponseEntity<CartResponseDto> removeItem(
            @PathVariable String userId,
            @PathVariable Long itemId
    ) {
        return ResponseEntity.ok(cartService.removeItem(userId, itemId));
    }

    @Operation(summary = "5. Clear all items")
    @DeleteMapping("/{userId}/items")
    public ResponseEntity<CartResponseDto> clearItems(@PathVariable String userId) {
        return ResponseEntity.ok(cartService.clearAllItems(userId));
    }

    @Operation(summary = "9. List all items in a cart")
    @GetMapping("/{userId}/items")
    public ResponseEntity<List<CartItemResponseDto>> listItems(@PathVariable String userId) {
        return ResponseEntity.ok(cartService.listItems(userId));
    }

    @Operation(summary = "7. Get cart summary")
    @GetMapping("/{userId}/summary")
    public ResponseEntity<CartSummaryDto> summary(@PathVariable String userId) {
        return ResponseEntity.ok(cartService.getSummary(userId));
    }

    @Operation(summary = "8. Delete entire cart")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteCart(@PathVariable String userId) {
        cartService.deleteCart(userId);
        return ResponseEntity.noContent().build();
    }
}
