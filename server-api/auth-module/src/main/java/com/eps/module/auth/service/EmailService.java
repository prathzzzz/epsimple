package com.eps.module.auth.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${email.from-address}")
    private String fromAddress;

    @Value("${email.from-name}")
    private String fromName;

    @Async
    public void sendPasswordResetEmail(String toEmail, String userName, String resetToken) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromAddress, fromName);
            helper.setTo(toEmail);
            helper.setSubject("Password Reset Request - EpsOne");

            String htmlContent = loadEmailTemplate("password-reset.html")
                    .replace("{{userName}}", userName)
                    .replace("{{resetToken}}", resetToken)
                    .replace("{{loginUrl}}", "http://epsone.electronicpay.in:9090/sign-in");
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Password reset email sent successfully to: {}", toEmail);
        } catch (MessagingException e) {
            log.error("Failed to send password reset email to: {}", toEmail, e);
            throw new RuntimeException("Failed to send password reset email", e);
        } catch (Exception e) {
            log.error("Unexpected error while sending email to: {}", toEmail, e);
            throw new RuntimeException("Failed to send email", e);
        }
    }

    @Async
    public void sendWelcomeEmail(String toEmail, String userName, String temporaryPassword) {
        log.info("[ASYNC] Starting to send welcome email to: {}", toEmail);
        try {
            log.debug("Creating MIME message for welcome email to: {}", toEmail);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromAddress, fromName);
            helper.setTo(toEmail);
            helper.setSubject("Welcome to EpsOne - Your Account Has Been Created");

            String htmlContent = loadEmailTemplate("welcome.html")
                    .replace("{{userName}}", userName)
                    .replace("{{userEmail}}", toEmail)
                    .replace("{{temporaryPassword}}", temporaryPassword)
                    .replace("{{loginUrl}}", "http://epsone.electronicpay.in:9090/sign-in");
            helper.setText(htmlContent, true);

            log.info("Sending welcome email via mail sender to: {}", toEmail);
            mailSender.send(message);
            log.info("[SUCCESS] Welcome email sent successfully to: {}", toEmail);
        } catch (MessagingException e) {
            log.error("Failed to send welcome email to: {}", toEmail, e);
            // Don't throw - user creation should succeed even if email fails
            log.warn("User created but welcome email failed for: {}", toEmail);
        } catch (Exception e) {
            log.error("Unexpected error while sending welcome email to: {}", toEmail, e);
            // Don't throw - user creation should succeed even if email fails
        }
    }

    /**
     * Load email template from resources
     */
    private String loadEmailTemplate(String templateName) {
        try {
            ClassPathResource resource = new ClassPathResource("email-templates/" + templateName);
            // Use InputStream instead of getFile() to work in JAR files
            try (var inputStream = resource.getInputStream()) {
                return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            log.error("Failed to load email template: {}", templateName, e);
            throw new RuntimeException("Failed to load email template: " + templateName, e);
        }
    }
}

            