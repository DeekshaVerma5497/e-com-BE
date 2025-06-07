package com.kalavastra.api.repository;

import com.kalavastra.api.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
	List<Order> findByUser_UserId(String userId);
}
