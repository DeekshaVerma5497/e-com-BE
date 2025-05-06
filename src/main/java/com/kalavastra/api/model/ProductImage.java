package com.kalavastra.api.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "product_images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore
    private Product product;

    @Column(name = "image_url", nullable = false, columnDefinition = "TEXT")
    private String imageUrl;

    @Builder.Default
    @Column(name = "is_primary")
    private Boolean isPrimary = false;

    @Builder.Default
    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "date_created", updatable = false)
    private Instant dateCreated;

    @Column(name = "date_updated")
    private Instant dateUpdated;

    @PrePersist
    protected void onCreate() {
        dateCreated = Instant.now();
        dateUpdated = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        dateUpdated = Instant.now();
    }
}
