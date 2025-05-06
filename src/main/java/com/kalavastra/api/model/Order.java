package com.kalavastra.api.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="order_id")
    private Long orderId;

    @Column(name="order_code", unique=true, nullable=false, length=50)
    private String orderCode;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id", referencedColumnName="user_id", nullable=false)
    private User user;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="address_id", nullable=false)
    private Address address;

    @Column(name="status", nullable=false, length=20)
    private String status;  // e.g. "Placed", "Cancelled"

    @Column(name="total_amount", nullable=false, precision=10, scale=2)
    private BigDecimal totalAmount;

    @OneToMany(mappedBy="order",
               cascade=CascadeType.ALL,
               orphanRemoval=true)
    @Builder.Default
    @JsonManagedReference
    private List<OrderItem> items = new ArrayList<>();

    @Column(name="date_created", updatable=false)
    private Instant dateCreated;

    @Column(name="date_updated")
    private Instant dateUpdated;

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        dateCreated = now;
        dateUpdated = now;
        if (orderCode==null || orderCode.isBlank()) {
            orderCode = "ORD-" + UUID.randomUUID().toString().substring(0,8);
        }
    }

    @PreUpdate
    protected void onUpdate() {
        dateUpdated = Instant.now();
    }
}
