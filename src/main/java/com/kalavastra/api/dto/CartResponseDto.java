package com.kalavastra.api.dto;

import lombok.*;

import java.util.List;

/**
 * Full cart returned on create/get/add/remove/update calls.
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CartResponseDto {

    private Long cartId;
    private String userId;
    private List<CartItemResponseDto> items;
}
