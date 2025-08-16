package org.example.remotly_ecommerce.service;

import org.example.remotly_ecommerce.dto.user.SignUpRequest;
import org.example.remotly_ecommerce.response.AuthResponse;
import org.example.remotly_ecommerce.dto.user.LoginRequest;
import org.example.remotly_ecommerce.response.OtpVerificationRequest;

import java.util.Optional;

/**
 * The {@code AuthService} interface defines the contract for authentication
 * and authorization operations in the system.
 * <p>
 * It provides methods for creating customer accounts, performing login with
 * password or OTP, and verifying OTP codes for authentication.
 * </p>
 *
 * <p><b>Responsibilities:</b></p>
 * <ul>
 *     <li>Register new customer accounts.</li>
 *     <li>Authenticate users with email/phone and password.</li>
 *     <li>Support login using One-Time Password (OTP).</li>
 *     <li>Verify OTPs and return authentication responses.</li>
 * </ul>
 *
 * @author Mohamed Sayed
 * @version 2.0
 * @since 2025-08-16
 */
public interface AuthService {

    /**
     * Creates a new customer account based on the provided sign-up request.
     *
     * @param request the {@link SignUpRequest} containing customer registration details
     * @return an {@link Optional} containing a JWT token if the registration is successful,
     *         or an empty {@link Optional} if registration fails
     */
    Optional<String> createCustomer(SignUpRequest request);

    /**
     * Performs user login using credentials such as email/phone and password.
     *
     * @param request the {@link LoginRequest} containing login details
     * @return a JWT token if authentication is successful
     */
    String login(LoginRequest request);

    /**
     * Performs login using a One-Time Password (OTP).
     *
     * @param request the {@link LoginRequest} containing login details for OTP-based authentication
     * @return a JWT token if OTP authentication is successful
     */
    String loginWithOtp(LoginRequest request);

    /**
     * Verifies the given OTP code for a specific user.
     *
     * @param request the {@link OtpVerificationRequest} containing OTP details
     * @return an {@link AuthResponse} containing authentication status and token if successful
     */
    AuthResponse verifyOtp(OtpVerificationRequest request);
}
