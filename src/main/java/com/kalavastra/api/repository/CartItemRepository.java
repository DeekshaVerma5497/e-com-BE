package com.kalavastra.api.repository;

import com.kalavastra.api.model.Cart;
import com.kalavastra.api.model.CartItem;
import com.kalavastra.api.model.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

	/** All items in the given cart */
	List<CartItem> findByCart(Cart cart);

	/** Only the items still marked active */
	List<CartItem> findByCartAndIsActive(Cart cart, boolean isActive);

	/** Look up a single item by its PK _and_ parent cart */
	Optional<CartItem> findByCartAndCartItemId(Cart cart, Long cartItemId);

	Optional<CartItem> findByCartAndProduct_IdAndIsActive(Cart cart, Long productId, Boolean isActive);

	/**
	 * Fetch active items in a cart along with their product.
	 */
	@Query("SELECT ci FROM CartItem ci JOIN FETCH ci.product p " + "WHERE ci.cart = :cart AND ci.isActive = true")
	List<CartItem> findActiveByCartWithProduct(@Param("cart") Cart cart);

	/**
	 * Find a single active CartItem by cart+product
	 */
	Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
}
