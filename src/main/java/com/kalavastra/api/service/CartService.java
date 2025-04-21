package com.kalavastra.api.service;

import com.kalavastra.api.dto.*;
import com.kalavastra.api.exception.ResourceNotFoundException;
import com.kalavastra.api.mapper.DomainMapper;
import com.kalavastra.api.model.*;
import com.kalavastra.api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Business logic for shopping cart: create, read, modify, delete.
 */
@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepo;
    private final CartItemRepository itemRepo;
    private final UserRepository userRepo;
    private final ProductService productService;      // add a method to return Product entity by code
    private final DomainMapper mapper;

    /**
     * 1. Create (or fetch) the default cart for a user.
     */
    @Transactional
    public Cart getOrCreateCart(String userId) {
        return cartRepo.findByUser_UserId(userId)
            .orElseGet(() -> {
                User user = userRepo.findByUserId(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
                Cart c = Cart.builder().user(user).build();
                return cartRepo.save(c);
            });
    }

    /**
     * 2. Get cart by user.
     */
    @Transactional
    public CartResponseDto getCart(String userId) {
        Cart cart = getOrCreateCart(userId);
        return mapper.cartToDto(cart);
    }

    /**
     * 3. Add a line (or bump existing).
     */
    @Transactional
    public CartResponseDto addItem(String userId, CartItemRequestDto req) {
        Cart cart = getOrCreateCart(userId);
        Product prod = productService.getEntityByCode(req.getProductCode());
        CartItem item = itemRepo.findByCartAndProduct(cart, prod)
            .orElseGet(() -> {
                CartItem ci = CartItem.builder()
                    .cart(cart).product(prod).quantity(0)
                    .build();
                cart.getItems().add(ci);
                return ci;
            });
        item.setQuantity(item.getQuantity() + req.getQuantity());
        itemRepo.save(item);
        return mapper.cartToDto(cart);
    }

    /**
     * 6. Update a given item’s quantity.
     */
    @Transactional
    public CartResponseDto updateItem(String userId, Long itemId, CartItemRequestDto req) {
        Cart cart = getOrCreateCart(userId);
        CartItem item = itemRepo.findById(itemId)
            .filter(ci -> ci.getCart().equals(cart))
            .orElseThrow(() -> new ResourceNotFoundException("CartItem", "id", itemId.toString()));
        item.setQuantity(req.getQuantity());
        itemRepo.save(item);
        return mapper.cartToDto(cart);
    }

    /**
     * 4. Remove a single line.
     */
    @Transactional
    public CartResponseDto removeItem(String userId, Long itemId) {
        Cart cart = getOrCreateCart(userId);
        CartItem item = itemRepo.findById(itemId)
            .filter(ci -> ci.getCart().equals(cart))
            .orElseThrow(() -> new ResourceNotFoundException("CartItem", "id", itemId.toString()));
        cart.getItems().remove(item);
        itemRepo.delete(item);
        return mapper.cartToDto(cart);
    }

    /**
     * 5. Clear all items.
     */
    @Transactional
    public CartResponseDto clearAllItems(String userId) {
        Cart cart = getOrCreateCart(userId);
        itemRepo.deleteAllByCart(cart);
        cart.getItems().clear();
        return mapper.cartToDto(cart);
    }

    /**
     * 7. Get cart summary.
     */
    @Transactional(readOnly = true)
    public CartSummaryDto getSummary(String userId) {
        Cart cart = getOrCreateCart(userId);
        return mapper.cartToSummaryDto(cart);
    }

    /**
     * 8. Delete entire cart.
     */
    @Transactional
    public void deleteCart(String userId) {
        cartRepo.deleteByUser_UserId(userId);
    }

    /**
     * 9. List all items.
     */
    @Transactional(readOnly = true)
    public List<CartItemResponseDto> listItems(String userId) {
        Cart cart = getOrCreateCart(userId);
        return mapper.cartItemsToItemDtos(cart.getItems());
    }
}
