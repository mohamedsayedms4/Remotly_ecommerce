package org.example.remotly_ecommerce.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a product in the e-commerce system.
 * Contains details such as title, description, pricing, images, category, seller, and reviews.
 *
 * Each product can have multiple images and reviews and belongs to a single category and seller.
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
public class Product {

    /**
     * Unique identifier for the product.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Title or name of the product.
     */
    private String title;

    /**
     * Detailed description of the product.
     */
    private String description;

    /**
     * Maximum Retail Price (MRP) of the product.
     */
    private Double mrpPrice;

    /**
     * Current selling price of the product after applying discount.
     */
    private Double sellingPrice;

    /**
     * Discount percentage applied to the product.
     */
    private Integer discountPercentage;

    /**
     * Available quantity in stock.
     */
    private Integer quantity;

    /**
     * Color variant of the product.
     */
    private String color;

    /**
     * List of image URLs associated with the product.
     */
    @ElementCollection
    @CollectionTable(
            name = "product_images",
            joinColumns = @JoinColumn(name = "product_id")
    )
    @Column(name = "image_url")
    private List<String> images = new ArrayList<>();

    /**
     * Number of ratings the product has received.
     */
    private Integer numRatings;

    /**
     * Category to which the product belongs.
     */
    @ManyToOne
    private Category category;

    /**
     * Seller who listed the product.
     */
    @ManyToOne
    private Seller seller;

    /**
     * Date and time when the product was created.
     */
    private LocalDateTime createdAt;

    /**
     * List of reviews written for the product.
     * Mapped by the product field in the Review entity.
     */
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    private Long views = 0L;

    private String size;

    private LocalDateTime updatedAt;


}
