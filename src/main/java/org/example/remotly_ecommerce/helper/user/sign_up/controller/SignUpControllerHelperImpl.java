package org.example.remotly_ecommerce.helper.user.sign_up.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.remotly_ecommerce.dto.user.SignUpRequest;
import org.example.remotly_ecommerce.utilis.ImageUploadUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
@Slf4j
public class SignUpControllerHelperImpl implements SignUpControllerHelper {

    private final ImageUploadUtil imageUploadUtil;

    @Override
    public SignUpRequest buildSignUpRequest(String userDetailsJson, MultipartFile image) throws Exception {
        log.info("seller_details : {} + image :{}", userDetailsJson ,image);

        ObjectMapper objectMapper = new ObjectMapper();
        SignUpRequest signUpRequest = objectMapper.readValue(userDetailsJson, SignUpRequest.class);
        log.info("signUpRequest : {}" ,signUpRequest.toString());

        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            imageUrl = imageUploadUtil.saveImage(image);
        }
        log.info("imageUrl : {}" ,imageUrl);


        return new SignUpRequest(
                signUpRequest.customerEmail(),
                signUpRequest.customerFullName(),
                signUpRequest.customerPhoneNumber(),
                imageUrl,
                signUpRequest.customerPassword()

        );

    }
}
