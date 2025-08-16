package org.example.remotly_ecommerce.helper.user.sign_in;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.remotly_ecommerce.exception.InvalidEmail;
import org.example.remotly_ecommerce.exception.InvalidPWD;
import org.example.remotly_ecommerce.model.User;
import org.example.remotly_ecommerce.repository.UserRepository;
import org.example.remotly_ecommerce.response.AuthResponse;
import org.example.remotly_ecommerce.dto.user.LoginRequest;
import org.example.remotly_ecommerce.service.impl.EmailService;
import org.example.remotly_ecommerce.utilis.OtpGeneratorUtil;
import org.example.remotly_ecommerce.utilis.SaveAndSendOtpUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link LoginStrategy} for authenticating users
 * using OTP (One-Time Password) sent via email.
 * <p>
 * This strategy validates the user's email and password, generates an OTP,
 * stores it, sends it to the user's email, and instructs the user to verify
 * the OTP for login completion.
 * </p>
 *
 * <p><b>Responsibilities:</b></p>
 * <ul>
 *     <li>Validate user email and password.</li>
 *     <li>Generate and store a new OTP for the user.</li>
 *     <li>Send the OTP to the user's email.</li>
 *     <li>Return an {@link AuthResponse} indicating OTP sent status.</li>
 * </ul>
 *
 * @author Mohamed Sayed
 * @version 2.0
 * @since 2025-08-16
 */
@Component("otpLoginStrategy")
@RequiredArgsConstructor
@Slf4j
public class OtpLoginStrategy implements LoginStrategy {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final SaveAndSendOtpUtil saveAndSendOtpUtil;

    /**
     * Initiates OTP-based login for a user.
     *
     * @param request the {@link LoginRequest} containing email and password
     * @return an {@link AuthResponse} with message about OTP sent status
     * @throws InvalidEmail if the email is empty or not found
     * @throws InvalidPWD if the password is empty or does not match
     */
    @Override
    public AuthResponse login(LoginRequest request) {
        log.info("login with customerEmail {}", request.userEmailCredentials());

        // 1) Validate input
        if (request.userEmailCredentials() == null || request.userEmailCredentials().isBlank()) {
            throw new InvalidEmail("Email must not be empty");
        }
        if (request.userPasswordCredentials() == null || request.userPasswordCredentials().isBlank()) {
            throw new InvalidPWD("Password must not be empty");
        }

        // 2) Find user by email
        User user = userRepository.findByEmail(request.userEmailCredentials())
                .orElseThrow(() -> {
                    log.error("User not found with customerEmail {}", request.userEmailCredentials());
                    return new InvalidEmail("Invalid customerEmail");
                });

        // 3) Validate password
        if (!passwordEncoder.matches(request.userPasswordCredentials(), user.getPassword())) {
            log.error("Password does not match for user {}", request.userEmailCredentials());
            throw new InvalidPWD("Invalid password");
        }

        // 4) Generate OTP
        String otp = OtpGeneratorUtil.generateOtp();

        // 5) Save or update OTP in DB
        saveAndSendOtpUtil.saveAndSendOtp(user, otp);

        // 6) Send OTP via email
        emailService.sendVerificationCode(
                user.getEmail(),
                otp,
                "Your OTP Code",
                "Use this code to login."
        );

        // 7) Build response
        AuthResponse response = new AuthResponse();
        response.setJwt(null); // JWT not yet generated
        response.setMessage("OTP sent to your customerEmail. Please verify.");
        response.setUserRole(null); // Role determined after OTP verification

        log.info("✅ OTP login initiated for user {} with role {}",
                user.getEmail(),
                user.getAuthorities().isEmpty() ? "NO_ROLE" : user.getAuthorities().iterator().next().getRole()
        );

        return response;
    }
}
