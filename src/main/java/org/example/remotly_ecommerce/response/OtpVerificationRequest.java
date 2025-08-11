package org.example.remotly_ecommerce.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class OtpVerificationRequest {
    private String email;
    private String otp;
}
