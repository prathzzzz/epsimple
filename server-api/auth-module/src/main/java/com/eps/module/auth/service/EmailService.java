package com.eps.module.auth.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
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

    @Async
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

    @Async
    public void sendWelcomeEmail(String toEmail, String userName, String temporaryPassword) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromAddress, fromName);
            helper.setTo(toEmail);
            helper.setSubject("Welcome to EpsOne - Your Account Has Been Created");

            String htmlContent = buildWelcomeEmailTemplate(userName, toEmail, temporaryPassword);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Welcome email sent successfully to: {}", toEmail);
        } catch (MessagingException e) {
            log.error("Failed to send welcome email to: {}", toEmail, e);
            // Don't throw - user creation should succeed even if email fails
            log.warn("User created but welcome email failed for: {}", toEmail);
        } catch (Exception e) {
            log.error("Unexpected error while sending welcome email to: {}", toEmail, e);
            // Don't throw - user creation should succeed even if email fails
        }
    }

    private String buildPasswordResetEmailTemplate(String userName, String resetToken) {
        // EPS Logo SVG (embedded as data URI)
        String epsLogo = """
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="48" height="48" fill="none" stroke="white" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <path d="M15 6v12a3 3 0 1 0 3-3H6a3 3 0 1 0 3 3V6a3 3 0 1 0-3 3h12a3 3 0 1 0-3-3"></path>
            </svg>
            """;
        
        return """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Password Reset - EpsOne</title>
                    <style>
                        * {
                            margin: 0;
                            padding: 0;
                            box-sizing: border-box;
                        }
                        body {
                            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
                            line-height: 1.6;
                            color: #1f2937;
                            background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%);
                            padding: 20px;
                        }
                        .email-wrapper {
                            max-width: 600px;
                            margin: 0 auto;
                            background: #ffffff;
                            border-radius: 16px;
                            overflow: hidden;
                            box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);
                        }
                        .header {
                            background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%);
                            color: white;
                            padding: 40px 30px;
                            text-align: center;
                        }
                        .logo {
                            margin-bottom: 20px;
                        }
                        .header h1 {
                            font-size: 28px;
                            font-weight: 700;
                            margin: 0;
                            letter-spacing: -0.5px;
                        }
                        .header p {
                            font-size: 16px;
                            opacity: 0.95;
                            margin-top: 8px;
                        }
                        .content {
                            padding: 40px 30px;
                            background: #ffffff;
                        }
                        .greeting {
                            font-size: 20px;
                            font-weight: 600;
                            color: #1f2937;
                            margin-bottom: 20px;
                        }
                        .message {
                            font-size: 16px;
                            color: #4b5563;
                            margin-bottom: 16px;
                        }
                        .token-container {
                            background: linear-gradient(135deg, #f3f4f6 0%%, #e5e7eb 100%%);
                            border-radius: 12px;
                            padding: 24px;
                            margin: 30px 0;
                            text-align: center;
                            border: 2px dashed #667eea;
                        }
                        .token-label {
                            font-size: 14px;
                            font-weight: 600;
                            color: #6b7280;
                            text-transform: uppercase;
                            letter-spacing: 1px;
                            margin-bottom: 12px;
                        }
                        .token {
                            font-size: 32px;
                            font-weight: 700;
                            color: #667eea;
                            letter-spacing: 4px;
                            font-family: 'Courier New', monospace;
                            user-select: all;
                        }
                        .expiry-notice {
                            display: inline-flex;
                            align-items: center;
                            gap: 8px;
                            background: #fef3c7;
                            color: #92400e;
                            padding: 12px 20px;
                            border-radius: 8px;
                            font-size: 14px;
                            font-weight: 600;
                            margin: 20px 0;
                        }
                        .warning-icon {
                            font-size: 18px;
                        }
                        .security-notice {
                            background: #eff6ff;
                            border-left: 4px solid #3b82f6;
                            padding: 16px 20px;
                            border-radius: 8px;
                            margin: 24px 0;
                        }
                        .security-notice p {
                            font-size: 14px;
                            color: #1e40af;
                            margin: 0;
                        }
                        .security-notice strong {
                            color: #1e3a8a;
                        }
                        .divider {
                            height: 1px;
                            background: linear-gradient(to right, transparent, #e5e7eb, transparent);
                            margin: 30px 0;
                        }
                        .footer {
                            background: #f9fafb;
                            padding: 30px;
                            text-align: center;
                            border-top: 1px solid #e5e7eb;
                        }
                        .footer-text {
                            font-size: 13px;
                            color: #6b7280;
                            margin: 8px 0;
                        }
                        .footer-brand {
                            font-size: 14px;
                            font-weight: 600;
                            color: #667eea;
                            margin-bottom: 8px;
                        }
                        .support-link {
                            color: #667eea;
                            text-decoration: none;
                            font-weight: 600;
                        }
                        .support-link:hover {
                            text-decoration: underline;
                        }
                        @media only screen and (max-width: 600px) {
                            body {
                                padding: 10px;
                            }
                            .header {
                                padding: 30px 20px;
                            }
                            .header h1 {
                                font-size: 24px;
                            }
                            .content {
                                padding: 30px 20px;
                            }
                            .token {
                                font-size: 24px;
                                letter-spacing: 2px;
                            }
                        }

                        /* Dark Mode Support */
                        @media (prefers-color-scheme: dark) {
                            body {
                                background: #111827 !important;
                            }
                            .email-wrapper {
                                background: #1f2937 !important;
                                color: #f3f4f6 !important;
                            }
                            .content {
                                background: #1f2937 !important;
                            }
                            .greeting {
                                color: #f9fafb !important;
                            }
                            .message {
                                color: #d1d5db !important;
                            }
                            .token-container {
                                background: #374151 !important;
                                border-color: #667eea !important;
                            }
                            .token-label {
                                color: #9ca3af !important;
                            }
                            .token {
                                color: #818cf8 !important;
                            }
                            .footer {
                                background: #111827 !important;
                                border-top-color: #374151 !important;
                            }
                            .footer-text {
                                color: #9ca3af !important;
                            }
                            .security-notice {
                                background: #1e3a8a !important;
                                border-left-color: #60a5fa !important;
                            }
                            .security-notice p {
                                color: #dbeafe !important;
                            }
                            .security-notice strong {
                                color: #bfdbfe !important;
                            }
                            .expiry-notice {
                                background: #451a03 !important;
                                color: #fef3c7 !important;
                            }
                        }
                    </style>
                </head>
                <body>
                    <div class="email-wrapper">
                        <!-- Header with Logo -->
                        <div class="header">
                            <div class="logo">
                                %s
                            </div>
                            <h1>Password Reset Request</h1>
                            <p>Secure access to your account</p>
                        </div>
                        
                        <!-- Main Content -->
                        <div class="content">
                            <div class="greeting">Hello %s,</div>
                            
                            <p class="message">
                                We received a request to reset the password for your <strong>EpsOne</strong> account.
                                To proceed with resetting your password, please use the verification code below.
                            </p>
                            
                            <!-- Token Display -->
                            <div class="token-container">
                                <div class="token-label">Your Verification Code</div>
                                <div class="token">%s</div>
                            </div>
                            
                            <center>
                                <div class="expiry-notice">
                                    <span class="warning-icon">⏱️</span>
                                    <span>This code expires in 1 hour</span>
                                </div>
                            </center>
                            
                            <!-- Security Notice -->
                            <div class="security-notice">
                                <p>
                                    <strong>🔒 Security Reminder:</strong> 
                                    Never share this code with anyone. EpsOne support will never ask for your verification code.
                                </p>
                            </div>
                            
                            <div class="divider"></div>
                            
                            <p class="message">
                                If you didn't request a password reset, you can safely ignore this email. 
                                Your password will remain unchanged.
                            </p>
                            
                            <p class="message">
                                If you have any concerns about your account security, please 
                                <a href="mailto:support@epsone.com" class="support-link">contact our support team</a> immediately.
                            </p>
                        </div>
                        
                        <!-- Footer -->
                        <div class="footer">
                            <div class="footer-brand">EpsOne</div>
                            <p class="footer-text">&copy; 2025 EpsOne. All rights reserved.</p>
                            <p class="footer-text">This is an automated message, please do not reply to this email.</p>
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(epsLogo, userName, resetToken);
    }

    private String buildWelcomeEmailTemplate(String userName, String userEmail, String temporaryPassword) {
        // EPS Logo SVG (embedded as data URI)
        String epsLogo = """
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="48" height="48" fill="none" stroke="white" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <path d="M15 6v12a3 3 0 1 0 3-3H6a3 3 0 1 0 3 3V6a3 3 0 1 0-3 3h12a3 3 0 1 0-3-3"></path>
            </svg>
            """;
        
        return """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Welcome to EpsOne</title>
                    <style>
                        * {
                            margin: 0;
                            padding: 0;
                            box-sizing: border-box;
                        }
                        body {
                            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
                            line-height: 1.6;
                            color: #1f2937;
                            background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%);
                            padding: 20px;
                        }
                        .email-wrapper {
                            max-width: 600px;
                            margin: 0 auto;
                            background: #ffffff;
                            border-radius: 16px;
                            overflow: hidden;
                            box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);
                        }
                        .header {
                            background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%);
                            color: white;
                            padding: 40px 30px;
                            text-align: center;
                        }
                        .logo {
                            margin-bottom: 20px;
                        }
                        .header h1 {
                            font-size: 28px;
                            font-weight: 700;
                            margin: 0;
                            letter-spacing: -0.5px;
                        }
                        .header p {
                            font-size: 16px;
                            opacity: 0.95;
                            margin-top: 8px;
                        }
                        .content {
                            padding: 40px 30px;
                            background: #ffffff;
                        }
                        .greeting {
                            font-size: 24px;
                            font-weight: 600;
                            color: #1f2937;
                            margin-bottom: 20px;
                        }
                        .message {
                            font-size: 16px;
                            color: #4b5563;
                            margin-bottom: 16px;
                        }
                        .credentials-container {
                            background: linear-gradient(135deg, #f3f4f6 0%%, #e5e7eb 100%%);
                            border-radius: 12px;
                            padding: 24px;
                            margin: 30px 0;
                            border: 2px solid #667eea;
                        }
                        .credential-row {
                            display: flex;
                            justify-content: space-between;
                            align-items: center;
                            padding: 12px 0;
                            border-bottom: 1px solid #d1d5db;
                        }
                        .credential-row:last-child {
                            border-bottom: none;
                        }
                        .credential-label {
                            font-size: 14px;
                            font-weight: 600;
                            color: #6b7280;
                            text-transform: uppercase;
                            letter-spacing: 0.5px;
                        }
                        .credential-value {
                            font-size: 16px;
                            font-weight: 600;
                            color: #1f2937;
                            font-family: 'Courier New', monospace;
                        }
                        .password-notice {
                            display: inline-flex;
                            align-items: center;
                            gap: 8px;
                            background: #fef3c7;
                            color: #92400e;
                            padding: 12px 20px;
                            border-radius: 8px;
                            font-size: 14px;
                            font-weight: 600;
                            margin: 20px 0;
                        }
                        .warning-icon {
                            font-size: 18px;
                        }
                        .security-notice {
                            background: #eff6ff;
                            border-left: 4px solid #3b82f6;
                            padding: 16px 20px;
                            border-radius: 8px;
                            margin: 24px 0;
                        }
                        .security-notice p {
                            font-size: 14px;
                            color: #1e40af;
                            margin: 0;
                        }
                        .security-notice strong {
                            color: #1e3a8a;
                        }
                        .cta-button {
                            display: inline-block;
                            background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%);
                            color: white;
                            padding: 14px 32px;
                            border-radius: 8px;
                            text-decoration: none;
                            font-weight: 600;
                            font-size: 16px;
                            margin: 24px 0;
                            transition: transform 0.2s;
                        }
                        .cta-button:hover {
                            transform: translateY(-2px);
                        }
                        .divider {
                            height: 1px;
                            background: linear-gradient(to right, transparent, #e5e7eb, transparent);
                            margin: 30px 0;
                        }
                        .footer {
                            background: #f9fafb;
                            padding: 30px;
                            text-align: center;
                            border-top: 1px solid #e5e7eb;
                        }
                        .footer-text {
                            font-size: 13px;
                            color: #6b7280;
                            margin: 8px 0;
                        }
                        .footer-brand {
                            font-size: 14px;
                            font-weight: 600;
                            color: #667eea;
                            margin-bottom: 8px;
                        }
                        .support-link {
                            color: #667eea;
                            text-decoration: none;
                            font-weight: 600;
                        }
                        .support-link:hover {
                            text-decoration: underline;
                        }
                        @media only screen and (max-width: 600px) {
                            body {
                                padding: 10px;
                            }
                            .header {
                                padding: 30px 20px;
                            }
                            .header h1 {
                                font-size: 24px;
                            }
                            .content {
                                padding: 30px 20px;
                            }
                            .credential-row {
                                flex-direction: column;
                                align-items: flex-start;
                                gap: 8px;
                            }
                        }

                        /* Dark Mode Support */
                        @media (prefers-color-scheme: dark) {
                            body {
                                background: #111827 !important;
                            }
                            .email-wrapper {
                                background: #1f2937 !important;
                                color: #f3f4f6 !important;
                            }
                            .content {
                                background: #1f2937 !important;
                            }
                            .greeting {
                                color: #f9fafb !important;
                            }
                            .message {
                                color: #d1d5db !important;
                            }
                            .credentials-container {
                                background: #374151 !important;
                                border-color: #4b5563 !important;
                            }
                            .credential-label {
                                color: #9ca3af !important;
                            }
                            .credential-value {
                                color: #f3f4f6 !important;
                            }
                            .footer {
                                background: #111827 !important;
                                border-top-color: #374151 !important;
                            }
                            .footer-text {
                                color: #9ca3af !important;
                            }
                            .security-notice {
                                background: #1e3a8a !important;
                                border-left-color: #60a5fa !important;
                            }
                            .security-notice p {
                                color: #dbeafe !important;
                            }
                            .security-notice strong {
                                color: #bfdbfe !important;
                            }
                            .password-notice {
                                background: #451a03 !important;
                                color: #fef3c7 !important;
                            }
                        }
                    </style>
                </head>
                <body>
                    <div class="email-wrapper">
                        <!-- Header with Logo -->
                        <div class="header">
                            <div class="logo">
                                %s
                            </div>
                            <h1>Welcome to EpsOne!</h1>
                            <p>Your account has been created successfully</p>
                        </div>
                        
                        <!-- Main Content -->
                        <div class="content">
                            <div class="greeting">Hello %s! 👋</div>
                            
                            <p class="message">
                                Welcome to <strong>EpsOne</strong>! Your account has been created and you're all set to get started.
                                Below are your login credentials:
                            </p>
                            
                            <!-- Credentials Display -->
                            <div class="credentials-container">
                                <div class="credential-row">
                                    <span class="credential-label">Email</span>
                                    <span class="credential-value">%s</span>
                                </div>
                                <div class="credential-row">
                                    <span class="credential-label">Temporary Password</span>
                                    <span class="credential-value">%s</span>
                                </div>
                            </div>
                            
                            <center>
                                <div class="password-notice">
                                    <span class="warning-icon">🔑</span>
                                    <span>Please change your password after first login</span>
                                </div>
                            </center>
                            
                            <!-- Security Notice -->
                            <div class="security-notice">
                                <p>
                                    <strong>🔒 Security Reminder:</strong> 
                                    For your security, we strongly recommend changing your temporary password immediately after logging in.
                                    Never share your password with anyone.
                                </p>
                            </div>
                            
                            <center>
                                <a href="http://epsone.electronicpay.in:9090/sign-in" class="cta-button">
                                    Login to Your Account
                                </a>
                            </center>
                            
                            <div class="divider"></div>
                            
                            <p class="message">
                                If you have any questions or need assistance, please don't hesitate to 
                                <a href="mailto:support@epsone.com" class="support-link">contact our support team</a>.
                            </p>
                            
                            <p class="message">
                                We're excited to have you on board!
                            </p>
                        </div>
                        
                        <!-- Footer -->
                        <div class="footer">
                            <div class="footer-brand">EpsOne</div>
                            <p class="footer-text">&copy; 2025 EpsOne. All rights reserved.</p>
                            <p class="footer-text">This is an automated message, please do not reply to this email.</p>
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(epsLogo, userName, userEmail, temporaryPassword);
    }
}
