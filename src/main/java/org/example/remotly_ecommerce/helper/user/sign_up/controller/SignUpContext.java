package org.example.remotly_ecommerce.helper.user.sign_up.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class SignUpContext {

    private final Map<String, SignUpStrategy> strategies;

    public SignUpStrategy getStrategy(String userType) {
        return strategies.get(userType);
    }
}