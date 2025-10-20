package com.eps.module.auth.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${email.from-address}")
    private String fromAddress;

    @Value("${email.from-name}")
    private String fromName;

    public void sendPasswordResetEmail(String toEmail, String userName, String resetToken) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromAddress, fromName);
            helper.setTo(toEmail);
            helper.setSubject("Password Reset Request - EpsOne");

            String htmlContent = buildPasswordResetEmailTemplate(userName, resetToken);
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

    private String buildPasswordResetEmailTemplate(String userName, String resetToken) {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            line-height: 1.6;
                            color: #333;
                        }
                        .container {
                            max-width: 600px;
                            margin: 0 auto;
                            padding: 20px;
                        }
                        .header {
                            background-color: #4CAF50;
                            color: white;
                            padding: 20px;
                            text-align: center;
                        }
                        .content {
                            padding: 20px;
                            background-color: #f9f9f9;
                        }
                        .token-box {
                            background-color: #fff;
                            border: 2px dashed #4CAF50;
                            padding: 15px;
                            margin: 20px 0;
                            text-align: center;
                            font-size: 24px;
                            font-weight: bold;
                            letter-spacing: 2px;
                            color: #4CAF50;
                        }
                        .warning {
                            color: #ff9800;
                            font-weight: bold;
                        }
                        .footer {
                            text-align: center;
                            padding: 20px;
                            font-size: 12px;
                            color: #777;
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1>Password Reset Request</h1>
                        </div>
                        <div class="content">
                            <p>Hello %s,</p>
                            <p>We received a request to reset your password for your EpsOne account.</p>
                            <p>Your password reset token is:</p>
                            <div class="token-box">%s</div>
                            <p class="warning">⚠️ This token will expire in 1 hour.</p>
                            <p>If you didn't request a password reset, please ignore this email or contact support if you have concerns.</p>
                            <p>For security reasons, never share this token with anyone.</p>
                        </div>
                        <div class="footer">
                            <p>&copy; 2025 EpsOne. All rights reserved.</p>
                            <p>This is an automated email, please do not reply.</p>
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(userName, resetToken);
    }
}
