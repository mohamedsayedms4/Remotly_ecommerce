package org.example.remotly_ecommerce.domain;

/**
 * Enum representing the roles that a user can have in the system.
 *
 * <p>These roles are used to define access levels and permissions
 * throughout the application.</p>
 *
 * <ul>
 *   <li>{@link #ROLE_ADMIN} - Has full access to all system features and administrative actions.</li>
 *   <li>{@link #ROLE_SELLER} - Can manage products, view orders, and handle their store operations.</li>
 *   <li>{@link #ROLE_CUSTOMER} - Can browse products, place orders, and manage their account.</li>
 * </ul>
 *
 * <p>This enum is typically used in conjunction with Spring Security
 * for authorization decisions.</p>
 *
 * <p>Example:</p>
 * <pre>
 * if (user.getRole() == UserRole.ROLE_ADMIN) {
 *     // Show admin dashboard
 * }
 * </pre>
 *
 * @author Mohamed Sayed
 * @since 27 July 2025
 */
public enum UserRole {

    /** Full system access and administrative privileges. */
    ROLE_ADMIN,

    /** Access to seller dashboard and product/order management. */
    ROLE_SELLER,

    /** Regular user with purchasing capabilities. */
    ROLE_CUSTOMER
}
