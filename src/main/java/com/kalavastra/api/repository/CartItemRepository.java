package com.kalavastra.api.repository;

import com.kalavastra.api.model.Cart;
import com.kalavastra.api.model.CartItem;
import com.kalavastra.api.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * JPA data‑access for CartItem lines.
 */
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);

    List<CartItem> findAllByCart(Cart cart);

    void deleteAllByCart(Cart cart);
}
