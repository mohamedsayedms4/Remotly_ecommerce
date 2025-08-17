package org.example.remotly_ecommerce.helper.user.sign_up.controller;

import org.example.remotly_ecommerce.dto.user.SignUpRequest;
import org.springframework.web.multipart.MultipartFile;

public interface SignUpControllerHelper {

    SignUpRequest buildSignUpRequest(String userDetailsJson, MultipartFile image) throws Exception;

}
