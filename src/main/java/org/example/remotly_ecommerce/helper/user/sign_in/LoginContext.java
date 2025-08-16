package org.example.remotly_ecommerce.helper.user.sign_in;

import lombok.RequiredArgsConstructor;
import org.example.remotly_ecommerce.response.AuthResponse;
import org.example.remotly_ecommerce.dto.user.LoginRequest;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Context class for executing login using different strategies.
 * <p>
 * This class uses the Strategy design pattern to support multiple login mechanisms,
 * such as email/password login or OTP login. It delegates the login request
 * to the appropriate {@link LoginStrategy} implementation.
 * </p>
 *
 * <p><b>Responsibilities:</b></p>
 * <ul>
 *     <li>Maintain a map of login strategies.</li>
 *     <li>Execute login using the selected strategy.</li>
 *     <li>Throw an exception if the requested strategy is unknown.</li>
 * </ul>
 *
 * @author Mohamed Sayed
 * @version 2.0
 * @since 2025-08-16
 */
@Service
@RequiredArgsConstructor
public class LoginContext {

    private final Map<String, LoginStrategy> strategies;

    /**
     * Executes login using the specified strategy type.
     *
     * @param type    the strategy name (e.g., "emailPasswordLoginStrategy", "otpLoginStrategy")
     * @param request the {@link LoginRequest} containing user credentials
     * @return an {@link AuthResponse} with JWT and user role if login is successful
     * @throws IllegalArgumentException if the strategy type is unknown
     */
    public AuthResponse executeLogin(String type, LoginRequest request) {
        LoginStrategy strategy = strategies.get(type);
        if (strategy == null) {
            throw new IllegalArgumentException("Unknown login strategy: " + type);
        }
        return strategy.login(request);
    }
}
