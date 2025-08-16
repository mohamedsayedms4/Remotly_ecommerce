package org.example.remotly_ecommerce.utilis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.remotly_ecommerce.model.User;
import org.example.remotly_ecommerce.model.VerificationCode;
import org.example.remotly_ecommerce.repository.VerificationCodeRepository;
import org.example.remotly_ecommerce.service.impl.EmailService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SaveAndSendOtpUtil {

    private final VerificationCodeRepository verificationCodeRepository;
    private final EmailService emailService;

    public void saveAndSendOtp(User user, String otp) {
        log.info("Starting OTP save and send process for user: {}", user.getEmail());

        VerificationCode verificationCode = verificationCodeRepository.findByEmail(user.getEmail());

        if (verificationCode == null) {
            log.debug("No existing verification code found for user: {}. Creating new record.", user.getEmail());
            verificationCode = new VerificationCode();
        } else {
            log.debug("Updating existing verification code for user: {}", user.getEmail());
        }

        verificationCode.setEmail(user.getEmail());
        verificationCode.setOtp(otp);
        verificationCode.setUser(user);

        verificationCodeRepository.save(verificationCode);
        log.info("OTP [{}] saved for user: {}", otp, user.getEmail());

        try {
            emailService.sendVerificationCode(
                    user.getEmail(),
                    otp,
                    "Your OTP Code",
                    "Use this code to login."
            );
            log.info("OTP customerEmail sent successfully to {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send OTP customerEmail to {}. Error: {}", user.getEmail(), e.getMessage(), e);
        }
    }
}
