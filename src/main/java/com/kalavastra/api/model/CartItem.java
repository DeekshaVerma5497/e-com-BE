package com.kalavastra.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "cart_items")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CartItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="cart_item_id")
    private Long cartItemId;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="cart_id", nullable=false)
    @JsonBackReference
    private Cart cart;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="product_id", nullable=false)
    private Product product;

    @Column(nullable=false)
    private Integer quantity;

    @Builder.Default
    @Column(nullable=false)
    private Boolean isActive = true;

    @Column(name="date_created", updatable=false)
    private Instant dateCreated;

    @Column(name="date_updated")
    private Instant dateUpdated;

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        dateCreated = now;
        dateUpdated = now;
    }

    @PreUpdate
    protected void onUpdate() {
        dateUpdated = Instant.now();
    }
}
