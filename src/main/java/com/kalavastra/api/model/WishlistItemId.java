package com.kalavastra.api.model;

import lombok.*;

import java.io.Serializable;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class WishlistItemId implements Serializable {
    private Long wishlist;
    private Long product;
}
