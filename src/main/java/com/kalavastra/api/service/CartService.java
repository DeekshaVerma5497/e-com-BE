package com.kalavastra.api.service;

import com.kalavastra.api.exception.ResourceNotFoundException;
import com.kalavastra.api.model.Cart;
import com.kalavastra.api.model.CartItem;
import com.kalavastra.api.model.Product;
import com.kalavastra.api.model.User;
import com.kalavastra.api.repository.CartItemRepository;
import com.kalavastra.api.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

	private final CartRepository cartRepo;
	private final CartItemRepository itemRepo;
	private final ProductService productService;
	private final UserService userService;

	/** <u>Strictly create</u>: error if already exists */
	@Transactional
	public Cart createCart(String userId) {
		if (cartRepo.existsByUser_UserId(userId)) {
			throw new IllegalStateException("Cart already exists for user " + userId);
		}
		User user = userService.getByUserId(userId);
		Cart c = Cart.builder().user(user).build();
		return cartRepo.save(c);
	}

	/** <u>Strictly fetch</u>: error if not found */
	@Transactional(readOnly = true)
	public Cart getCart(String userId) {
		return cartRepo.findByUser_UserId(userId)
				.orElseThrow(() -> new ResourceNotFoundException("Cart", "userId", userId));
	}

	/**
	 * Adjusts a cart‐item’s quantity by delta (positive or negative). If the new
	 * quantity < 1, the item is removed entirely.
	 */
	@Transactional
	public Cart adjustItemQuantity(String userId, Long itemId, int delta) {
		Cart cart = getCart(userId);

		CartItem item = itemRepo.findById(itemId).filter(ci -> ci.getCart().equals(cart))
				.orElseThrow(() -> new ResourceNotFoundException("CartItem", "id", itemId.toString()));

		int newQty = item.getQuantity() + delta;
		if (newQty < 1) {
			// remove the entry
			cart.getItems().remove(item);
			itemRepo.delete(item);
		} else {
			item.setQuantity(newQty);
			itemRepo.save(item);
		}

		return cart;
	}

	/** delete entire cart */
	@Transactional
	public void deleteCart(String userId) {
		Cart c = getCart(userId);
		cartRepo.delete(c);
	}

	/** add or update quantity of a product */
	@Transactional
	public Cart addItem(String userId, String productCode, int qty) {
		Cart cart = getCart(userId);
		Product p = productService.getByCode(productCode);

		CartItem item = itemRepo.findByCartAndProduct(cart, p).orElseGet(() -> {
			var ci = CartItem.builder().cart(cart).product(p).build();
			cart.getItems().add(ci);
			return ci;
		});

		item.setQuantity(qty);
		item.setIsActive(true);
		itemRepo.save(item);
		return cart;
	}

	/** list all items in the cart */
	@Transactional(readOnly = true)
	public List<CartItem> listItems(String userId) {
		return getCart(userId).getItems();
	}

	/** remove one item */
	@Transactional
	public Cart removeItem(String userId, Long itemId) {
		Cart cart = getCart(userId);
		CartItem item = itemRepo.findById(itemId).filter(ci -> ci.getCart().equals(cart))
				.orElseThrow(() -> new ResourceNotFoundException("CartItem", "id", itemId.toString()));
		cart.getItems().remove(item);
		itemRepo.delete(item);
		return cart;
	}
}
