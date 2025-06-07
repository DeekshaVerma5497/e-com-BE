package com.kalavastra.api.service;

import com.kalavastra.api.exception.ResourceNotFoundException;
import com.kalavastra.api.model.*;
import com.kalavastra.api.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
	private final OrderRepository orderRepo;
	private final UserService userService;
	private final AddressService addressService;
	private final ProductService productService;

	@Transactional
	public Order place(String userId, Long addressId, List<OrderItem> items) {
		var user = userService.getByUserId(userId);
		var address = addressService.get(addressId);
		Order order = Order.builder().user(user).address(address).status("PLACED").build();

		BigDecimal total = BigDecimal.ZERO;
		for (OrderItem oi : items) {
			var p = productService.getByCode(oi.getProduct().getProductCode());
			oi.setOrder(order);
			oi.setProduct(p);
			oi.setPrice(p.getPrice());
			total = total.add(p.getPrice().multiply(BigDecimal.valueOf(oi.getQuantity())));
			order.getItems().add(oi);
		}
		order.setTotalAmount(total);
		return orderRepo.save(order);
	}

	@Transactional(readOnly = true)
	public Order getById(Long id) {
		return orderRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Order", "orderId", id.toString()));
	}

	@Transactional(readOnly = true)
	public List<Order> listForUser(String userId) {
		userService.getByUserId(userId);
		return orderRepo.findByUser_UserId(userId);
	}

	@Transactional
	public Order cancel(Long orderId) {
		Order o = getById(orderId);
		o.setStatus("CANCELLED");
		return orderRepo.save(o);
	}
}
