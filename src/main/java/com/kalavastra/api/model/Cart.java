package com.kalavastra.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Cart {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="cart_id")
    private Long cartId;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id", referencedColumnName="user_id", nullable=false)
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy="cart",
               cascade=CascadeType.ALL,
               orphanRemoval=true)
    @Builder.Default
    @JsonManagedReference
    private List<CartItem> items = new ArrayList<>();

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
