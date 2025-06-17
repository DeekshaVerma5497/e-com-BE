package com.kalavastra.api.service;

import com.kalavastra.api.model.OrderItem;
import com.kalavastra.api.model.OrderReturn;
import com.kalavastra.api.repository.OrderItemRepository;
import com.kalavastra.api.repository.OrderReturnRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderReturnService {

	private final OrderReturnRepository returnRepo;
	private final OrderItemRepository itemRepo;

	@Transactional(readOnly = true)
	public List<OrderReturn> listReturns(String userId) {
		return returnRepo.findByOrderItemOrderUserId(userId);
	}

	@Transactional(readOnly = true)
	public OrderReturn getReturn(String userId, Long returnId) {
		OrderReturn ret = returnRepo.findById(returnId)
				.orElseThrow(() -> new IllegalArgumentException("Return not found: " + returnId));
		if (!ret.getOrderItem().getOrder().getUserId().equals(userId)) {
			throw new SecurityException("Not authorized to view this return");
		}
		return ret;
	}

	@Transactional
	public OrderReturn requestReturn(String userId, Long orderItemId, OrderReturn incoming) {
		OrderItem item = itemRepo.findById(orderItemId)
				.orElseThrow(() -> new IllegalArgumentException("OrderItem not found: " + orderItemId));
		if (!item.getOrder().getUserId().equals(userId)) {
			throw new SecurityException("Not authorized to return this item");
		}

		incoming.setOrderItem(item);
		incoming.setStatus("REQUESTED");
		// timestamps handled by Hibernate

		return returnRepo.save(incoming);
	}

	@Transactional
	public OrderReturn updateReturn(String userId, Long returnId, OrderReturn updates) {
		OrderReturn existing = getReturn(userId, returnId);
		// allow user to update reason, refund details or status
		existing.setReason(updates.getReason());
		existing.setRefundAmount(updates.getRefundAmount());
		existing.setRefundMethod(updates.getRefundMethod());
		existing.setStatus(updates.getStatus());
		return returnRepo.save(existing);
	}

	@Transactional
	public OrderReturn cancelReturn(String userId, Long returnId) {
		OrderReturn existing = getReturn(userId, returnId);
		existing.setStatus("CANCELLED");
		return returnRepo.save(existing);
	}
}
