package com.kalavastra.api.dto;

import lombok.*;

import java.math.BigDecimal;

/**
 * One line returned when you fetch a cart or list items.
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CartItemResponseDto {

    private Long cartItemId;
    private String productCode;
    private String name;
    private BigDecimal unitPrice;
    private Integer quantity;
    private BigDecimal lineTotal; // unitPrice * quantity
}
