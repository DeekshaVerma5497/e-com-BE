package com.kalavastra.api.service;

import com.kalavastra.api.model.Order;
import com.kalavastra.api.model.OrderItem;
import com.kalavastra.api.repository.OrderItemRepository;
import com.kalavastra.api.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
	private final OrderRepository orderRepo;
	private final OrderItemRepository itemRepo;

	@Transactional(readOnly = true)
	public List<Order> listOrders(String userId) {
		return orderRepo.findByUserId(userId);
	}

	@Transactional(readOnly = true)
	public Order getOrder(String userId, Long orderId) {
		Order order = orderRepo.findById(orderId)
				.orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));
		if (!order.getUserId().equals(userId)) {
			throw new SecurityException("Not authorized to view this order");
		}
		return order;
	}

	@Transactional
	public Order placeOrder(String userId, Order incoming) {
		// assign the logged-in user
		incoming.setUserId(userId);
		// you can tweak status logic if needed
		incoming.setStatus("PLACED");
		// generate a unique code if none provided
		if (incoming.getOrderCode() == null) {
			incoming.setOrderCode("ORD-" + UUID.randomUUID().toString().substring(0, 8));
		}
		// persist order
		Order saved = orderRepo.save(incoming);

		// link & save items
		for (OrderItem it : incoming.getItems()) {
			it.setOrder(saved);
			itemRepo.save(it);
		}

		saved.setItems(itemRepo.findByOrder(saved));
		return saved;
	}

	@Transactional
	public Order cancelOrder(String userId, Long orderId) {
		Order order = getOrder(userId, orderId);
		order.setStatus("CANCELLED");
		return orderRepo.save(order);
	}
}
