package com.kalavastra.api.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Product entity representing individual sarees or items listed in the catalog.
 */
@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Business-friendly SKU/Code for internal or customer reference.
     */
    @Column(name = "product_code", unique = true, nullable = false, length = 50)
    private String productCode;

    /**
     * Product display title.
     */
    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 500)
    private String description;

    /**
     * Price in INR.
     */
    @Column(nullable = false)
    private BigDecimal price;

    /**
     * Stock available.
     */
    @Column(nullable = false)
    private Integer stock;

    /**
     * Product image URL (hosted on CDN or cloud).
     */
    @Column(name = "image_url", length = 255)
    private String imageUrl;

    /**
     * Foreign key to category.
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="category_id", nullable=false)
    @JsonIgnore
    private Category category;

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
