package org.example.remotly_ecommerce.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

/**
 * Represents an address entity that stores location details
 * including city, state, and zip code.
 *
 * This entity can be used for user or seller addresses in the system.
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
public class Address {

    /**
     * The unique identifier for the address.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name or label of the address (e.g., "Home", "Work").
     */
    private String name;

    /**
     * The locality or neighborhood of the address.
     */
    private String locality;

    /**
     * The state in which the address is located.
     */
    private String state;

    /**
     * The city in which the address is located.
     */
    private String city;

    /**
     * The detailed street address.
     */
    private String address;

    /**
     * The postal or ZIP code of the address.
     */
    private String zip;
}
