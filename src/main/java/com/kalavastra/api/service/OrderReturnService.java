package com.kalavastra.api.service;

import com.kalavastra.api.exception.ResourceNotFoundException;
import com.kalavastra.api.model.OrderReturn;
import com.kalavastra.api.model.OrderItem;
import com.kalavastra.api.repository.OrderReturnRepository;
import com.kalavastra.api.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderReturnService {

    private final OrderReturnRepository returnRepo;
    private final OrderItemRepository    itemRepo;

    /** 1. Request a return for an order item (only if it belongs to that user) */
    @Transactional
    public OrderReturn requestReturn(
            String userId,
            Long orderItemId,
            String reason,
            String refundMethod
    ) {
        OrderItem item = itemRepo.findById(orderItemId)
            .filter(oi -> oi.getOrder().getUser().getUserId().equals(userId))
            .orElseThrow(() ->
                new ResourceNotFoundException("OrderItem", "id", orderItemId.toString())
            );

        // compute full line refund
        BigDecimal refundAmount = item.getPrice()
            .multiply(BigDecimal.valueOf(item.getQuantity()));

        OrderReturn r = OrderReturn.builder()
            .orderItem(item)
            .reason(reason)
            .refundMethod(refundMethod)
            .refundAmount(refundAmount)
            .build();

        return returnRepo.save(r);
    }

    /** 2. Fetch one return by its ID */
    @Transactional(readOnly = true)
    public OrderReturn getReturn(Long returnId) {
        return returnRepo.findById(returnId)
            .orElseThrow(() ->
                new ResourceNotFoundException("OrderReturn","returnId",returnId.toString())
            );
    }

    /** 3. List all returns for a given user */
    @Transactional(readOnly = true)
    public List<OrderReturn> listForUser(String userId) {
        return returnRepo.findByOrderItem_Order_User_UserId(userId);
    }

    /** 4. List all returns for a given order */
    @Transactional(readOnly = true)
    public List<OrderReturn> listForOrder(Long orderId) {
        return returnRepo.findByOrderItem_Order_OrderId(orderId);
    }

    /** 5. Update status, refund method, or amount for an existing return */
    @Transactional
    public OrderReturn updateReturn(
        String userId,
        Long returnId,
        String status,
        String refundMethod
    ) {
        OrderReturn r = returnRepo.findById(returnId)
            .filter(rr -> rr.getOrderItem().getOrder()
                               .getUser().getUserId().equals(userId))
            .orElseThrow(() ->
                new ResourceNotFoundException("OrderReturn", "returnId", returnId.toString())
            );
        if (status != null && !status.isBlank()) {
            r.setStatus(status);
        }
        if (refundMethod != null && !refundMethod.isBlank()) {
            r.setRefundMethod(refundMethod);
        }
        // (we leave refundAmount alone here, or recompute it if needed)
        return returnRepo.save(r);
    }
}
