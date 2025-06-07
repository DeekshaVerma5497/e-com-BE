package com.kalavastra.api.repository;

import com.kalavastra.api.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
	List<Wishlist> findByUser_UserId(String userId);
}
