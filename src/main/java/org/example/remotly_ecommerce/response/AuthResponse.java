package org.example.remotly_ecommerce.response;


import lombok.Data;
import org.example.remotly_ecommerce.domain.UserRole;

/**
 * Represents the response returned after a successful authentication.
 * This response typically includes a JWT token, a message, and the user's role.
 *
 * Example usage:
 * <pre>
 *     AuthResponse response = new AuthResponse();
 *     response.setJwt("eyJhbGciOiJIUzI1NiIsInR...");
 *     response.setMessage("Login successful");
 *     response.setUserRole(UserRole.ROLE_CUSTOMER);
 * </pre>
 *
 * This class is used in login or signup APIs.
 *
 * @author Mohamed Sayed
 * @since 2025-07-27
 */
@Data
public class AuthResponse {
    /**
     * The JWT token generated upon successful authentication.
     */
    private String jwt;

    /**
     * A custom message to be returned to the client (e.g., success message).
     */
    private String message;

    /**
     * The role of the authenticated user (e.g., ROLE_CUSTOMER, ROLE_SELLER).
     */
    private UserRole userRole;
}
