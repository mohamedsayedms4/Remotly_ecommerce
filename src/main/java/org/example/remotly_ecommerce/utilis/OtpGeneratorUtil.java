package org.example.remotly_ecommerce.utilis;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class OtpGeneratorUtil {
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final int OTP_LENGTH = 6;

    public static String generateOtp() {
        int number = secureRandom.nextInt(1_000_000);
        return String.format("%0" + OTP_LENGTH + "d", number);
    }
}
