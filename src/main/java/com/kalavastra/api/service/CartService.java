package com.kalavastra.api.service;

import com.kalavastra.api.model.Cart;
import com.kalavastra.api.model.CartItem;
import com.kalavastra.api.repository.CartItemRepository;
import com.kalavastra.api.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

	private final CartRepository cartRepo;
	private final CartItemRepository itemRepo;
	private final ProductService productService;

	@Transactional(readOnly = true)
	public List<CartItem> listItems(String userId) {
		return cartRepo.findByUserId(userId).map(cart -> {
			var active = itemRepo.findByCartAndIsActive(cart, true);
			// fully hydrate each product
			active.forEach(ci -> ci.setProduct(productService.getById(ci.getProductId())));
			return active;
		}).orElse(List.of());
	}

	@Transactional
	public List<CartItem> addItem(String userId, CartItem incoming) {
		Cart cart = cartRepo.findByUserId(userId).orElseGet(() -> cartRepo.save(Cart.builder().userId(userId).build()));

		Long prodId = incoming.getProductId();
		Optional<CartItem> existing = itemRepo.findByCartAndProduct_IdAndIsActive(cart, prodId, true);

		if (existing.isPresent()) {
			CartItem ci = existing.get();
			ci.setQuantity(ci.getQuantity() + incoming.getQuantity());
			itemRepo.save(ci);
		} else {
			incoming.setCart(cart);
			incoming.setIsActive(true);
			itemRepo.save(incoming);
		}

		return listItems(userId);
	}

	@Transactional
	public List<CartItem> removeItem(String userId, Long itemId) {
		Cart cart = cartRepo.findByUserId(userId).orElseThrow(() -> new IllegalArgumentException("Cart not found"));
		CartItem toRem = itemRepo.findByCartAndCartItemId(cart, itemId)
				.orElseThrow(() -> new IllegalArgumentException("Item not in cart"));
		toRem.setIsActive(false);
		itemRepo.save(toRem);
		return listItems(userId);
	}

	@Transactional
	public List<CartItem> clearCart(String userId) {
		Cart cart = cartRepo.findByUserId(userId).orElseThrow(() -> new IllegalArgumentException("Cart not found"));
		var active = itemRepo.findByCartAndIsActive(cart, true);
		active.forEach(ci -> ci.setIsActive(false));
		itemRepo.saveAll(active);
		return List.of();
	}
}
