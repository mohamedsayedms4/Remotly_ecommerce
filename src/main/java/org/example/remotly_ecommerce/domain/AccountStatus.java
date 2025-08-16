package org.example.remotly_ecommerce.domain;

/**
 * Represents the status of a user or seller account in the system.
 * This is used to manage and control access based on account condition.
 *
 * @author Mohamed Sayed
 * @since 27 July 2025
 */
public enum AccountStatus {

    /** Account is created but waiting for customerEmail or phone verification. */
    PENDING_VERIFICATION,

    /** Account is active and can use the system without restriction. */
    ACTIVE,

    /** Account is temporarily suspended, possibly due to violations or unpaid dues. */
    SUSPENDED,

    /** Account was intentionally deactivated by the user or admin. */
    DEACTIVATED,

    /** Account is banned due to serious violations (e.g., fraud). */
    BANNED,

    /** Account is permanently closed and cannot be reactivated. */
    CLOSED
}
