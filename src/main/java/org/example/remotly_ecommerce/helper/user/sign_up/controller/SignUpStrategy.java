package org.example.remotly_ecommerce.helper.user.sign_up.controller;

import org.example.remotly_ecommerce.dto.user.SignUpRequest;
import org.example.remotly_ecommerce.response.AuthResponse;

public interface SignUpStrategy {
    AuthResponse signUp(SignUpRequest request) throws Exception;

}
