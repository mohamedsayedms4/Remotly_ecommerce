package org.example.remotly_ecommerce.helper.user.sign_in;

import org.example.remotly_ecommerce.response.AuthResponse;
import org.example.remotly_ecommerce.dto.user.LoginRequest;

/**
 * The {@code LoginStrategy} interface defines a strategy for performing user login.
 * <p>
 * Implementations of this interface can define different login mechanisms,
 * such as email/password login, OTP login, or other custom authentication strategies.
 * </p>
 *
 * <p><b>Responsibilities:</b></p>
 * <ul>
 *     <li>Authenticate a user based on the provided login request.</li>
 *     <li>Return an {@link AuthResponse} containing authentication details, such as JWT token and user role.</li>
 * </ul>
 *
 * @author Mohamed Sayed
 * @version 2.0
 * @since 2025-08-16
 */
public interface LoginStrategy {

    /**
     * Performs login using the provided {@link LoginRequest}.
     *
     * @param request the login request containing credentials or OTP
     * @return an {@link AuthResponse} containing JWT token, user role, and message
     */
    AuthResponse login(LoginRequest request);
}
