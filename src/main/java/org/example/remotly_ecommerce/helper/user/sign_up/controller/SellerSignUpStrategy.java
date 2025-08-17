package org.example.remotly_ecommerce.helper.user.sign_up.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.remotly_ecommerce.domain.UserRole;
import org.example.remotly_ecommerce.dto.user.SignUpRequest;
import org.example.remotly_ecommerce.response.AuthResponse;
import org.example.remotly_ecommerce.service.SellerService;
import org.springframework.stereotype.Component;

@Component("sellerSignUpStrategy")
@RequiredArgsConstructor
@Slf4j
public class SellerSignUpStrategy implements SignUpStrategy {

    private final SellerService sellerService;

    @Override
    public AuthResponse signUp(SignUpRequest request) {
        String jwt = sellerService.createSeller(request)
                .orElseThrow(() -> new RuntimeException("Failed to create seller"));

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("Seller created successfully");
        authResponse.setUserRole(UserRole.ROLE_SELLER);
        log.info("Seller created successfully");

        return authResponse;
    }
}