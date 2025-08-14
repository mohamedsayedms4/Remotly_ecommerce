package org.example.remotly_ecommerce.service.impl;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    public void sendVerificationCode(String email, String otp, String subject, String body) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setSubject(subject);
            helper.setTo(email);

            String content = body + "\nYour verification code is: " + otp;

            helper.setText(content, false);

            mailSender.send(mimeMessage);
        } catch (MailException e) {
            throw new MailSendException("Failed to send verification code", e);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}
