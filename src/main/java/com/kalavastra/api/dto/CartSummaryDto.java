package com.kalavastra.api.dto;

import lombok.*;

import java.math.BigDecimal;

/**
 * Lightweight summary of a cart.
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CartSummaryDto {

    private Long cartId;
    private String userId;
    private int distinctItemCount;
    private int totalQuantity;
    private BigDecimal totalPrice;
}
