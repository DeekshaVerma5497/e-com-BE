package com.kalavastra.api.repository;

import com.kalavastra.api.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * JPA data-access for Cart.
 */
public interface CartRepository extends JpaRepository<Cart, Long> {

    /** Lookup the (single) cart for a given business‑ID user. */
    Optional<Cart> findByUser_UserId(String userId);

    /** Delete by user level ID. */
    void deleteByUser_UserId(String userId);
}
