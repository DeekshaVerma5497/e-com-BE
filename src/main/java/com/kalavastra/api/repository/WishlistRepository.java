package com.kalavastra.api.repository;

import com.kalavastra.api.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
	Optional<Wishlist> findByUserId(String userId);
}
