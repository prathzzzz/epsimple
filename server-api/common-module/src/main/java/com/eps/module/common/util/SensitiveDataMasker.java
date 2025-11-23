package com.eps.module.common.util;

import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.regex.Pattern;

/**
 * Utility class for masking sensitive data in logs to ensure security and compliance.
 * Masks fields like passwords, tokens, PII (email, phone, PAN, Aadhaar) before logging.
 */
@Component
public class SensitiveDataMasker {

    private static final String MASK = "****";

    // Sensitive field patterns (case-insensitive)
    private static final Set<String> SENSITIVE_FIELDS = Set.of(
            "password", "pwd", "pass", "passwd",
            "token", "jwt", "accesstoken", "refreshtoken", "bearer",
            "otp", "otpcode", "otpvalue",
            "pan", "pancard", "pannumber",
            "aadhaar", "aadhar", "aadharnumber", "aadhaarnumber",
            "email", "emailaddress", "emailid",
            "phone", "mobile", "phonenumber", "mobilenumber", "contact", "contactnumber"
    );

    /**
     * Masks sensitive query parameters in URL query strings.
     * Example: password=secret123&name=John → password=****&name=John
     *
     * @param queryString The URL query string to mask
     * @return Masked query string with sensitive values replaced
     */
    public String maskQueryParams(String queryString) {
        if (queryString == null || queryString.isEmpty()) {
            return "";
        }

        String[] params = queryString.split("&");
        StringBuilder masked = new StringBuilder();

        for (String param : params) {
            if (masked.length() > 0) {
                masked.append("&");
            }

            String[] keyValue = param.split("=", 2);
            String key = keyValue[0];

            if (isSensitiveField(key)) {
                masked.append(key).append("=").append(MASK);
            } else {
                masked.append(param);
            }
        }

        return masked.toString();
    }

    /**
     * Masks sensitive fields in JSON request/response body.
     * Example: {"password":"secret","name":"John"} → {"password":"****","name":"John"}
     *
     * @param body The JSON body to mask
     * @return Masked JSON body with sensitive values replaced
     */
    public String maskRequestBody(String body) {
        if (body == null || body.isEmpty()) {
            return "";
        }

        String result = body;

        // Regex-based masking for JSON fields
        // Matches: "fieldName":"value" or "fieldName": "value" or "fieldName" : "value"
        for (String field : SENSITIVE_FIELDS) {
            // Pattern for string values: "field":"value" or 'field':'value'
            String stringPattern = "[\"']" + field + "[\"']\\s*:\\s*[\"'][^\"']*[\"']";
            String stringReplacement = "\"" + field + "\":\"" + MASK + "\"";
            result = result.replaceAll("(?i)" + stringPattern, stringReplacement);

            // Pattern for non-quoted values (less common): "field":value
            String nonQuotedPattern = "[\"']" + field + "[\"']\\s*:\\s*[^,}\\]\\s]+";
            String nonQuotedReplacement = "\"" + field + "\":\"" + MASK + "\"";
            result = result.replaceAll("(?i)" + nonQuotedPattern, nonQuotedReplacement);
        }

        return result;
    }

    /**
     * Checks if a field name is considered sensitive.
     *
     * @param fieldName The field name to check
     * @return true if the field is sensitive, false otherwise
     */
    private boolean isSensitiveField(String fieldName) {
        if (fieldName == null) {
            return false;
        }
        
        String lowerFieldName = fieldName.toLowerCase();
        
        // Check if the field name contains any sensitive keyword
        return SENSITIVE_FIELDS.stream()
                .anyMatch(lowerFieldName::contains);
    }

    /**
     * Masks email addresses in text.
     * Example: user@example.com → u***@example.com
     *
     * @param text Text containing email addresses
     * @return Text with masked email addresses
     */
    public String maskEmails(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        // Simple email masking pattern
        Pattern emailPattern = Pattern.compile("([a-zA-Z0-9._%+-]+)@([a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})");
        return emailPattern.matcher(text).replaceAll(match -> {
            String localPart = match.group(1);
            String domain = match.group(2);
            return localPart.charAt(0) + MASK + "@" + domain;
        });
    }

    /**
     * Masks phone numbers in text.
     * Example: 9876543210 → 98******10
     *
     * @param text Text containing phone numbers
     * @return Text with masked phone numbers
     */
    public String maskPhoneNumbers(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        // Pattern for 10-digit phone numbers
        Pattern phonePattern = Pattern.compile("\\b(\\d{2})(\\d{6})(\\d{2})\\b");
        return phonePattern.matcher(text).replaceAll("$1" + MASK + "$3");
    }
}
