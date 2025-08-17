package org.example.remotly_ecommerce.helper.user.sign_up.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.remotly_ecommerce.domain.UserRole;
import org.example.remotly_ecommerce.dto.user.SignUpRequest;
import org.example.remotly_ecommerce.response.AuthResponse;
import org.example.remotly_ecommerce.service.AuthService;
import org.springframework.stereotype.Component;

@Component("userSignUpStrategy")
@RequiredArgsConstructor
@Slf4j
public class UserSignUpStrategy implements SignUpStrategy {

    private final AuthService authService;

    @Override
    public AuthResponse signUp(SignUpRequest request) {
        String jwt = authService.createCustomer(request).
                orElseThrow(() -> new RuntimeException("Failed to create customer"));

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("User created successfully");
        authResponse.setUserRole(UserRole.ROLE_CUSTOMER);

        return authResponse;
    }
}