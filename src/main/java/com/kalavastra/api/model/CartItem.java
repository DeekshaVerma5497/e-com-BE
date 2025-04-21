package com.kalavastra.api.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

/**
 * One line in a Cart tying a Product to a quantity.
 */
@Entity
@Table(name = "cart_items")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private Long cartItemId;

    /** Owning cart. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    /** The product in this line. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    /** How many units. */
    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "date_created", updatable = false)
    private Instant dateCreated;

    @Column(name = "date_updated")
    private Instant dateUpdated;
    
    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

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
