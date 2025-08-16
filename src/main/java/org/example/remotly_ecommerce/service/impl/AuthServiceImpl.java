package org.example.remotly_ecommerce.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.remotly_ecommerce.domain.UserRole;
import org.example.remotly_ecommerce.dto.user.SignUpRequest;
import org.example.remotly_ecommerce.helper.user.sign_in.LoginContext;
import org.example.remotly_ecommerce.helper.user.sign_up.Register;
import org.example.remotly_ecommerce.model.User;
import org.example.remotly_ecommerce.model.VerificationCode;
import org.example.remotly_ecommerce.repository.VerificationCodeRepository;
import org.example.remotly_ecommerce.response.AuthResponse;
import org.example.remotly_ecommerce.dto.user.LoginRequest;
import org.example.remotly_ecommerce.response.OtpVerificationRequest;
import org.example.remotly_ecommerce.service.AuthService;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementation of the {@link AuthService} interface responsible for handling
 * user authentication and registration workflows.
 * <p>
 * This service integrates with login strategies, OTP verification, JWT token
 * generation, and user registration mechanisms.
 * </p>
 *
 * <p><b>Responsibilities:</b></p>
 * <ul>
 *     <li>Registers new customers using {@link Register} helper.</li>
 *     <li>Supports login using email & password strategy.</li>
 *     <li>Supports login using One-Time Password (OTP) strategy.</li>
 *     <li>Verifies OTPs, generates JWT tokens, and retrieves user roles.</li>
 * </ul>
 *
 * @author Mohamed Sayed
 * @version 2.0
 * @since 2025-08-16
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final VerificationCodeRepository verificationCodeRepository;
    private final JwtService jwtService;
    private final LoginContext loginContext;
    private final Register register;

    /**
     * Registers a new customer account by delegating to the {@link Register} helper.
     *
     * @param request the {@link SignUpRequest} containing customer details
     * @return an {@link Optional} containing a JWT token if successful
     */
    @Override
    public Optional<String> createCustomer(SignUpRequest request) {
        log.info("Create customer request: {}", request);
        SignUpRequest customerRequest = new SignUpRequest(
                request.customerEmail(),
                request.customerFullName(),
                request.customerPhoneNumber(),
                request.customerProfileImage(),
                request.customerPassword()
        );
        return register.registerAccount(customerRequest, UserRole.ROLE_CUSTOMER.toString());
    }

    /**
     * Performs login using the email/password login strategy.
     *
     * @param request the {@link LoginRequest} containing login credentials
     * @return a JWT token if authentication is successful
     */
    @Override
    public String login(LoginRequest request) {
        AuthResponse authResponse = loginContext.executeLogin("emailPasswordLoginStrategy", request);
        return authResponse.getJwt();
    }

    /**
     * Performs login using the One-Time Password (OTP) login strategy.
     *
     * @param request the {@link LoginRequest} containing OTP login details
     * @return a JWT token if OTP authentication is successful
     */
    @Override
    public String loginWithOtp(LoginRequest request) {
        AuthResponse response = loginContext.executeLogin("otpLoginStrategy", request);
        return response.getJwt();
    }

    /**
     * Verifies the provided OTP for the given email and generates a JWT token if valid.
     * <p>
     * The OTP is validated against the stored record, and once verified, it is deleted.
     * </p>
     *
     * @param request the {@link OtpVerificationRequest} containing email and OTP code
     * @return an {@link AuthResponse} with JWT token, message, and assigned user role
     * @throws RuntimeException if no OTP is found, OTP is invalid, or user has no authorities
     */
    @Override
    public AuthResponse verifyOtp(OtpVerificationRequest request) {
        try {
            String email = request.getEmail();
            String otp = request.getOtp();

            VerificationCode verificationCode = verificationCodeRepository.findByEmail(email);
            if (verificationCode == null) {
                throw new RuntimeException("No OTP request found for this customerEmail");
            }

            if (!verificationCode.getOtp().equals(otp)) {
                throw new RuntimeException("Invalid OTP code");
            }

            User user = verificationCode.getUser();
            verificationCodeRepository.delete(verificationCode);

            String jwtToken = jwtService.generateToken(user);

            UserRole userRole;
            if (user.getAuthorities() != null && !user.getAuthorities().isEmpty()) {
                userRole = user.getAuthorities().iterator().next().getRole();
            } else {
                throw new RuntimeException("User has no authorities assigned");
            }

            AuthResponse response = new AuthResponse();
            response.setJwt(jwtToken);
            response.setMessage("OTP verified successfully");
            response.setUserRole(userRole);

            return response;

        } catch (Exception e) {
            log.error("Error verifying OTP", e);
            throw e;
        }
    }
}
