package com.kalavastra.api.repository;

import com.kalavastra.api.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
	/** Lookup a user's cart by their user-id (VARCHAR) */
	Optional<Cart> findByUserId(String userId);
}
