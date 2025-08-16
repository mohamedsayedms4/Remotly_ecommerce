package org.example.remotly_ecommerce.helper.user.sign_in;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.remotly_ecommerce.domain.UserRole;
import org.example.remotly_ecommerce.exception.InvalidEmail;
import org.example.remotly_ecommerce.exception.InvalidPWD;
import org.example.remotly_ecommerce.model.User;
import org.example.remotly_ecommerce.repository.UserRepository;
import org.example.remotly_ecommerce.response.AuthResponse;
import org.example.remotly_ecommerce.dto.user.LoginRequest;
import org.example.remotly_ecommerce.service.impl.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link LoginStrategy} for authenticating users
 * using email and password credentials.
 * <p>
 * This strategy validates the user's email and password against
 * the database and generates a JWT token upon successful authentication.
 * </p>
 *
 * <p><b>Responsibilities:</b></p>
 * <ul>
 *     <li>Authenticate user using email and password.</li>
 *     <li>Generate JWT token for authenticated users.</li>
 *     <li>Return an {@link AuthResponse} containing JWT, message, and user role.</li>
 * </ul>
 *
 * @author Mohamed Sayed
 * @version 2.0
 * @since 2025-08-16
 */
@Component("emailPasswordLoginStrategy")
@RequiredArgsConstructor
@Slf4j
public class EmailPasswordLoginStrategy implements LoginStrategy {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    /**
     * Authenticates a user using email and password.
     *
     * @param request the {@link LoginRequest} containing email and password
     * @return an {@link AuthResponse} containing JWT, user role, and message
     * @throws InvalidEmail if the email is not found in the database
     * @throws InvalidPWD if the password does not match the stored hash
     */
    @Override
    public AuthResponse login(LoginRequest request) {
        log.info("login with customerEmail {}", request.userEmailCredentials());

        User user = userRepository.findByEmail(request.userEmailCredentials())
                .orElseThrow(() -> {
                    log.info("user not found with customerEmail {}", request.userEmailCredentials());
                    return new InvalidEmail("Invalid customerEmail");
                });

        if (!passwordEncoder.matches(request.userPasswordCredentials(), user.getPassword())) {
            log.info("passwords don't match");
            throw new InvalidPWD("Invalid password");
        }

        String jwtToken = jwtService.generateToken(user);
        UserRole role = user.getAuthorities().iterator().next().getRole();

        AuthResponse response = new AuthResponse();
        response.setJwt(jwtToken);
        response.setMessage("Login successful");
        response.setUserRole(role);

        log.info("login successful with customerEmail {} with Role {}", request.userEmailCredentials(), role);
        return response;
    }
}
