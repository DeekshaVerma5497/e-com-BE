package com.kalavastra.api.repository;

import com.kalavastra.api.model.OrderReturn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderReturnRepository extends JpaRepository<OrderReturn, Long> {
	/** All returns belonging to orders of a given user */
	List<OrderReturn> findByOrderItemOrderUserId(String userId);
}
