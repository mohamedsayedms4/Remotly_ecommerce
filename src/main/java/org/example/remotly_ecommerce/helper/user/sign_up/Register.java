package org.example.remotly_ecommerce.helper.user.sign_up;

import org.example.remotly_ecommerce.dto.user.SignUpRequest;

import java.util.Optional;

/**
 * The {@code Register} interface provides a contract for registering
 * new user accounts with a specific role in the system.
 * <p>
 * Implementations of this interface should handle the business logic
 * required for creating and persisting user accounts during the
 * sign-up process.
 * </p>
 *
 * @author Mohamed Sayed
 * @version 2.0
 * @since 2025-08-16
 */
public interface Register {

    /**
     * Registers a new user account based on the provided sign-up request
     * and assigns the given role.
     *
     * @param request the sign-up request containing user details
     * @param role    the role to be assigned to the new account
     * @return an {@link Optional} containing a success message or identifier
     *         if registration succeeds, or an empty {@link Optional}
     *         if the process fails
     */
    Optional<String> registerAccount(SignUpRequest request, String role);

}
