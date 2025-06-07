package com.kalavastra.api.repository;

import com.kalavastra.api.model.OrderReturn;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderReturnRepository extends JpaRepository<OrderReturn, Long> {
	List<OrderReturn> findByOrderItem_Order_User_UserId(String userId);

	List<OrderReturn> findByOrderItem_Order_OrderId(Long orderId);
}
