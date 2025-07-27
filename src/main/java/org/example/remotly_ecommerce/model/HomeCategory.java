package org.example.remotly_ecommerce.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.example.remotly_ecommerce.domain.HomeCategorySection;

/**
 * Represents a specific category item used for the homepage display.
 * Each HomeCategory is used to visually organize the home page sections
 * such as grid, deals, electronics, etc.
 *
 * This is a persistent entity mapped to the database.
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
public class HomeCategory {

    /**
     * The unique identifier for the home category (primary key).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the category to be displayed on the homepage.
     */
    private String name;

    /**
     * A short description of the category.
     */
    private String description;

    /**
     * Icon (possibly as a class name or URL) representing the category.
     */
    private String icon;

    /**
     * Image URL or path used to represent the category visually.
     */
    private String image;

    /**
     * The ID of the linked category (reference to backend category entity).
     */
    private String categoryId;

    /**
     * Section of the homepage where this category will be displayed.
     */
    private HomeCategorySection homeCategorySection;
}
