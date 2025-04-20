package com.kalavastra.api.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.List;

/**
 * Category entity to represent product groupings like Sarees, Accessories etc.
 */
@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Business-level category identifier (e.g., sarees_silk)
     */
    @Column(name = "category_code", unique = true, nullable = false, length = 50)
    private String categoryCode;

    /**
     * Display name of the category (e.g., "Silk Sarees")
     */
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * Optional description for admin use or UI display
     */
    @Column(length = 255)
    private String description;

    /**
     * One-to-many relationship with products.
     */
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Product> products;

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
