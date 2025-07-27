package org.example.remotly_ecommerce.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Represents a financial transaction between a customer and a seller.
 * Each transaction is linked to a {@link User} as the customer and a {@link Seller}.
 *
 * This entity can be used to track when a transaction occurred and who was involved.
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
public class Transaction {

    /**
     * Unique identifier for the transaction.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The customer (user) who is involved in the transaction.
     */
    @ManyToOne
    private User Customer;

    /**
     * The seller involved in the transaction.
     */
    @OneToOne
    private Seller Seller;

    /**
     * The date and time when the transaction was created.
     * Initialized to the current system time.
     */
    private LocalDateTime Date = LocalDateTime.now();
}
