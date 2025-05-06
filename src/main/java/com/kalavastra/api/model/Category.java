package com.kalavastra.api.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "categories")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Category {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="category_code", nullable=false, unique=true, length=50)
    private String categoryCode;

    @Column(nullable=false, length=100)
    private String name;

    @Column(columnDefinition="TEXT")
    private String description;

    @Builder.Default
    @Column(name="is_active", nullable=false)
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
