package com.eps.module.common.util;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.NoIvGenerator;

/**
 * Utility class to encrypt and decrypt sensitive properties using Jasypt.
 * 
 * This class is used to generate encrypted values for properties files.
 * The encrypted values are then stored in properties files with ENC() wrapper.
 * 
 * Usage:
 * 1. Set JASYPT_ENCRYPTOR_PASSWORD environment variable in Windows
 * 2. Run this class with the plain text value as argument
 * 3. Copy the encrypted output to your properties file wrapped in ENC()
 * 
 * Example:
 * - Plain: myPassword123
 * - Encrypted: ENC(encrypted_value_here)
 */
public class JasyptEncryptor {
    
    private static final String ALGORITHM = "PBEWithMD5AndDES";
    
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("╔════════════════════════════════════════════════════════════╗");
            System.out.println("║          Jasypt Property Encryptor Utility                 ║");
            System.out.println("╚════════════════════════════════════════════════════════════╝");
            System.out.println();
            System.out.println("Usage: java JasyptEncryptor <plain-text-value> [environment]");
            System.out.println();
            System.out.println("Arguments:");
            System.out.println("  plain-text-value : The value to encrypt");
            System.out.println("  environment      : 'dev' or 'prod' (optional, defaults to 'dev')");
            System.out.println();
            System.out.println("Examples:");
            System.out.println("  java JasyptEncryptor \"myPassword123\"");
            System.out.println("  java JasyptEncryptor \"myPassword123\" dev");
            System.out.println("  java JasyptEncryptor \"myPassword123\" prod");
            System.out.println();
            System.out.println("Prerequisites:");
            System.out.println("  1. Set environment variables:");
            System.out.println("     - JASYPT_ENCRYPTOR_PASSWORD_DEV (for dev)");
            System.out.println("     - JASYPT_ENCRYPTOR_PASSWORD_PROD (for prod)");
            System.out.println("  2. Restart your terminal/IDE after setting the variables");
            System.out.println();
            System.out.println("Output format: ENC(encrypted_value)");
            System.out.println("Copy the entire output including ENC() to your properties file");
            System.out.println();
            return;
        }
        
        String plainText = args[0];
        String environment = args.length > 1 ? args[1].toLowerCase() : "dev";
        
        // Determine which environment variable to use
        String envVarName;
        if ("prod".equals(environment)) {
            envVarName = "JASYPT_ENCRYPTOR_PASSWORD_PROD";
        } else {
            envVarName = "JASYPT_ENCRYPTOR_PASSWORD_DEV";
        }
        
        String password = System.getenv(envVarName);
        
        if (password == null || password.trim().isEmpty()) {
            System.err.println("❌ ERROR: " + envVarName + " environment variable is not set!");
            System.err.println();
            System.err.println("Please set the environment variable first:");
            System.err.println();
            System.err.println("For DEV environment:");
            System.err.println("Windows (PowerShell - Run as Administrator):");
            System.err.println("  [System.Environment]::SetEnvironmentVariable('JASYPT_ENCRYPTOR_PASSWORD_DEV', 'your-dev-secret-key', [System.EnvironmentVariableTarget]::Machine)");
            System.err.println();
            System.err.println("For PROD environment:");
            System.err.println("Windows (PowerShell - Run as Administrator):");
            System.err.println("  [System.Environment]::SetEnvironmentVariable('JASYPT_ENCRYPTOR_PASSWORD_PROD', 'your-prod-secret-key', [System.EnvironmentVariableTarget]::Machine)");
            System.err.println();
            System.err.println("Windows (Command Prompt - Permanent):");
            System.err.println("  setx " + envVarName + " \"your-secret-key\" /M");
            System.err.println();
            System.err.println("Then restart your terminal/IDE and try again.");
            System.exit(1);
        }
        
        try {
            StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
            encryptor.setPassword(password);
            encryptor.setAlgorithm(ALGORITHM);
            encryptor.setIvGenerator(new NoIvGenerator());
            
            String encrypted = encryptor.encrypt(plainText);
            
            System.out.println("╔════════════════════════════════════════════════════════════╗");
            System.out.println("║          Encryption Successful ✓                           ║");
            System.out.println("╚════════════════════════════════════════════════════════════╝");
            System.out.println();
            System.out.println("Environment: " + environment.toUpperCase());
            System.out.println("Plain text:  " + plainText);
            System.out.println();
            System.out.println("Encrypted value (copy this to properties file):");
            System.out.println("ENC(" + encrypted + ")");
            System.out.println();
            System.out.println("Example usage in properties file:");
            System.out.println("  spring.datasource.password=ENC(" + encrypted + ")");
            System.out.println();
            
            // Verify decryption
            String decrypted = encryptor.decrypt(encrypted);
            if (plainText.equals(decrypted)) {
                System.out.println("✓ Verification: Encryption/Decryption successful!");
            } else {
                System.err.println("⚠ Warning: Decrypted value doesn't match original!");
            }
            
        } catch (Exception e) {
            System.err.println("❌ ERROR: Encryption failed!");
            System.err.println("Error message: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    /**
     * Encrypts a plain text string using Jasypt
     * @param plainText The text to encrypt
     * @param password The encryption password
     * @return Encrypted string
     */
    public static String encrypt(String plainText, String password) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(password);
        encryptor.setAlgorithm(ALGORITHM);
        encryptor.setIvGenerator(new NoIvGenerator());
        return encryptor.encrypt(plainText);
    }
    
    /**
     * Decrypts an encrypted string using Jasypt
     * @param encryptedText The encrypted text (without ENC() wrapper)
     * @param password The encryption password
     * @return Decrypted string
     */
    public static String decrypt(String encryptedText, String password) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(password);
        encryptor.setAlgorithm(ALGORITHM);
        encryptor.setIvGenerator(new NoIvGenerator());
        return encryptor.decrypt(encryptedText);
    }
}
