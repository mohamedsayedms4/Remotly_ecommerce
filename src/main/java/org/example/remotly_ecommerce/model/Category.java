package org.example.remotly_ecommerce.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Entity representing a product category in the system.
 * Categories can be hierarchical, where a category can have a parent category.
 *
 * Useful for organizing products in nested levels (e.g., Electronics > Mobile > Accessories).
 *
 * Fields include a unique categoryId, level, and optional parent category.
 *
 * @author Mohamed Sayed
 * @since 2025-07-27
 */
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Category {

    /**
     * Primary key of the category entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Human-readable name of the category (e.g., "Electronics").
     */
    private String name;

    /**
     * Unique identifier string for this category used internally.
     */
    @NotNull
    @Column(unique = true)
    private String categoryId;

    /**
     * Reference to the parent category (if any), allowing hierarchical structure.
     */
    @ManyToOne
    private Category parentCategory;

    /**
     * The level or depth of this category in the hierarchy (e.g., 0 for top-level).
     */
    @NotNull
    private Integer level;
}
