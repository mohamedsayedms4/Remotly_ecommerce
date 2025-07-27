package org.example.remotly_ecommerce.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a product review submitted by a user.
 * Contains the review text, rating, optional images, and references to the product and user.
 *
 * Each review is linked to one product and one user.
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
public class Review {

    /**
     * Unique identifier for the review.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Text content of the review.
     */
    @Column(nullable = false)
    private String reviewTxt;

    /**
     * Rating score given by the user (e.g., from 1.0 to 5.0).
     */
    @Column(nullable = false)
    private Double rating;

    /**
     * Optional list of image URLs uploaded by the user as part of the review.
     */
    @ElementCollection
    private List<String> productImages;

    /**
     * Product that is being reviewed.
     * Ignored in JSON serialization to avoid circular references.
     */
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    /**
     * User who wrote the review.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Timestamp when the review was created.
     */
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
