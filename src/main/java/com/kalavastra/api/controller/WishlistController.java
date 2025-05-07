package com.kalavastra.api.controller;

import com.kalavastra.api.model.*;
import com.kalavastra.api.service.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users/{userId}/wishlists")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService svc;

    @Operation(summary = "Create a new wishlist for a user")
    @PostMapping
    public ResponseEntity<Wishlist> create(
        @PathVariable String userId,
        @RequestParam String name
    ) {
        return ResponseEntity.ok(svc.createWishlist(userId, name));
    }

    @Operation(summary = "List all wishlists for a user")
    @GetMapping
    public ResponseEntity<List<Wishlist>> list(
        @PathVariable String userId
    ) {
        return ResponseEntity.ok(svc.listWishlists(userId));
    }

    @Operation(summary = "Rename a wishlist")
    @PutMapping("/{wishlistId}")
    public ResponseEntity<Wishlist> rename(
        @PathVariable String userId,
        @PathVariable Long wishlistId,
        @RequestParam String name
    ) {
        return ResponseEntity.ok(
            svc.renameWishlist(userId, wishlistId, name)
        );
    }

    @Operation(summary = "Delete a wishlist")
    @DeleteMapping("/{wishlistId}")
    public ResponseEntity<Void> delete(
        @PathVariable String userId,
        @PathVariable Long wishlistId
    ) {
        svc.deleteWishlist(userId, wishlistId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "List items in a wishlist")
    @GetMapping("/{wishlistId}/items")
    public ResponseEntity<List<WishlistItem>> listItems(
        @PathVariable String userId,
        @PathVariable Long wishlistId
    ) {
        return ResponseEntity.ok(
            svc.listItems(userId, wishlistId)
        );
    }

    @Operation(summary = "Add a product to a wishlist")
    @PostMapping("/{wishlistId}/items")
    public ResponseEntity<Wishlist> addItem(
        @PathVariable String userId,
        @PathVariable Long wishlistId,
        @RequestParam String productCode
    ) {
        return ResponseEntity.ok(
            svc.addItem(userId, wishlistId, productCode)
        );
    }

    @Operation(summary = "Remove a product from a wishlist")
    @DeleteMapping("/{wishlistId}/items")
    public ResponseEntity<Void> removeItem(
        @PathVariable String userId,
        @PathVariable Long wishlistId,
        @RequestParam String productCode
    ) {
        svc.removeItem(userId, wishlistId, productCode);
        return ResponseEntity.noContent().build();
    }
}
