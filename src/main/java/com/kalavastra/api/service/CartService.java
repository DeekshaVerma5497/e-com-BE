package com.kalavastra.api.service;

import com.kalavastra.api.exception.ResourceNotFoundException;
import com.kalavastra.api.model.Cart;
import com.kalavastra.api.model.CartItem;
import com.kalavastra.api.model.Product;
import com.kalavastra.api.repository.CartItemRepository;
import com.kalavastra.api.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepo;
    private final CartItemRepository itemRepo;
    private final ProductService productService;
    private final UserService userService;

    @Transactional
    public Cart getOrCreate(String userId) {
        var user = userService.getByUserId(userId);
        return cartRepo.findByUser_UserId(userId)
            .orElseGet(() -> cartRepo.save(
                Cart.builder().user(user).build()
            ));
    }

    @Transactional(readOnly=true)
    public Cart getCart(String userId) {
        return getOrCreate(userId);
    }

    @Transactional
    public Cart addItem(String userId, String productCode, int qty) {
        Cart cart = getOrCreate(userId);
        Product p = productService.getByCode(productCode);
        CartItem item = itemRepo.findByCartAndProduct(cart,p)
            .orElseGet(() -> {
                var ci = CartItem.builder()
                    .cart(cart)
                    .product(p)
                    .build();
                cart.getItems().add(ci);
                return ci;
            });
        item.setQuantity(qty);
        item.setIsActive(true);
        itemRepo.save(item);
        return cart;
    }

    @Transactional
    public Cart updateItem(String userId, Long itemId, int qty) {
        Cart cart = getOrCreate(userId);
        CartItem item = itemRepo.findById(itemId)
            .filter(ci->ci.getCart().equals(cart))
            .orElseThrow(() -> new ResourceNotFoundException("CartItem","id",itemId.toString()));
        item.setQuantity(qty);
        itemRepo.save(item);
        return cart;
    }

    @Transactional
    public Cart removeItem(String userId, Long itemId) {
        Cart cart = getOrCreate(userId);
        CartItem item = itemRepo.findById(itemId)
            .filter(ci->ci.getCart().equals(cart))
            .orElseThrow(() -> new ResourceNotFoundException("CartItem","id",itemId.toString()));
        cart.getItems().remove(item);
        itemRepo.delete(item);
        return cart;
    }

    @Transactional
    public void clearCart(String userId) {
        Cart cart = getOrCreate(userId);
        cart.getItems().forEach(itemRepo::delete);
        cart.getItems().clear();
    }

    @Transactional
    public void deleteCart(String userId) {
        Cart cart = getOrCreate(userId);
        cartRepo.delete(cart);
    }
}
