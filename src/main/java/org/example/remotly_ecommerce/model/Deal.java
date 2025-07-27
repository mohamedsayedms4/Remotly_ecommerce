package org.example.remotly_ecommerce.model;


import jakarta.persistence.*;
import lombok.*;

/**
 * Represents a promotional deal linked to a specific home category.
 * Used to feature or highlight deals on the home page.
 *
 * Example use case: A "Deal of the Day" section displaying products
 * from a particular category.
 *
 * This entity maintains a one-to-one relationship with {@link HomeCategory}.
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
public class Deal {

    /**
     * Unique identifier for the deal.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The home category associated with this deal.
     * Represents the featured category in the deal.
     */
    @OneToOne
    private HomeCategory homeCategory;
}
