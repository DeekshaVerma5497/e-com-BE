package com.kalavastra.api.repository;

import com.kalavastra.api.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Long> {
    Optional<Cart> findByUser_UserId(String userId);
    boolean existsByUser_UserId(String userId);
}
