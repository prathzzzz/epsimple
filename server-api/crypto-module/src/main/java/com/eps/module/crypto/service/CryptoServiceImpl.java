package com.eps.module.crypto.service;

import com.eps.module.crypto.config.CryptoConfiguration;
import com.eps.module.crypto.exception.CryptoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HexFormat;

/**
 * Implementation of CryptoService using AES-256-GCM for encryption and SHA-256 for hashing.
 * This service is designed to be generic and reusable across any project.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CryptoServiceImpl implements CryptoService {

    private static final String ENCRYPTION_ALGORITHM = "AES/GCM/NoPadding";
    private static final String KEY_ALGORITHM = "AES";
    private static final String HASH_ALGORITHM = "SHA-256";
    private static final int GCM_TAG_LENGTH = 128; // bits
    private static final int GCM_IV_LENGTH = 12; // bytes (96 bits recommended for GCM)
    private static final int KEY_SIZE = 32; // 256 bits

    private final CryptoConfiguration cryptoConfiguration;
    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    public String encrypt(String plaintext) {
        if (plaintext == null || plaintext.isEmpty()) {
            return plaintext;
        }

        try {
            // Generate a secret key from the configured encryption key
            SecretKey secretKey = deriveKey(cryptoConfiguration.getEncryptionKey());

            // Generate a random IV
            byte[] iv = new byte[GCM_IV_LENGTH];
            secureRandom.nextBytes(iv);

            // Initialize cipher
            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);

            // Encrypt the data
            byte[] encryptedData = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

            // Prepend IV to encrypted data
            byte[] combined = new byte[iv.length + encryptedData.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(encryptedData, 0, combined, iv.length, encryptedData.length);

            // Return Base64 encoded result
            return Base64.getEncoder().encodeToString(combined);

        } catch (Exception e) {
            log.error("Encryption failed", e);
            throw new CryptoException("Failed to encrypt data", e);
        }
    }

    @Override
    public boolean isEncrypted(String data) {
        if (data == null || data.isEmpty()) {
            return false;
        }
        
        // Check if it looks like Base64 encoded data with sufficient length
        // Encrypted data should be at least IV (12 bytes) + tag (16 bytes) + some data = ~40+ Base64 chars
        if (data.length() < 40) {
            return false;
        }
        
        // Check if it contains only valid Base64 characters
        return data.matches("^[A-Za-z0-9+/]*={0,2}$");
    }

    @Override
    public String decrypt(String encryptedData) {
        if (encryptedData == null || encryptedData.isEmpty()) {
            log.debug("Decrypt called with null or empty value, returning as-is");
            return encryptedData;
        }

        // If data doesn't look encrypted, return as-is (backward compatibility with plain text)
        if (!isEncrypted(encryptedData)) {
            log.warn("Attempted to decrypt data that doesn't appear to be encrypted. Returning as-is. Data: '{}'", encryptedData);
            return encryptedData;
        }

        try {
            log.debug("Attempting to decrypt data (length: {} chars)", encryptedData.length());
            
            // Decode Base64
            byte[] combined = Base64.getDecoder().decode(encryptedData);
            log.debug("Base64 decoded, combined length: {} bytes", combined.length);

            // Extract IV and encrypted data
            byte[] iv = new byte[GCM_IV_LENGTH];
            byte[] ciphertext = new byte[combined.length - GCM_IV_LENGTH];
            log.debug("Extracting IV ({}  bytes) and ciphertext ({} bytes)", GCM_IV_LENGTH, ciphertext.length);
            
            if (ciphertext.length < 0) {
                log.error("Invalid encrypted data: ciphertext length is negative ({}). Combined length: {}, IV length: {}", 
                    ciphertext.length, combined.length, GCM_IV_LENGTH);
                log.error("This usually means the input is not encrypted data. Input value: '{}'", encryptedData);
                throw new IllegalArgumentException("Invalid encrypted data: data too short or not encrypted");
            }
            
            System.arraycopy(combined, 0, iv, 0, GCM_IV_LENGTH);
            System.arraycopy(combined, GCM_IV_LENGTH, ciphertext, 0, ciphertext.length);

            // Generate secret key
            SecretKey secretKey = deriveKey(cryptoConfiguration.getEncryptionKey());

            // Initialize cipher for decryption
            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);

            // Decrypt the data
            byte[] decryptedData = cipher.doFinal(ciphertext);

            return new String(decryptedData, StandardCharsets.UTF_8);

        } catch (Exception e) {
            log.error("Decryption failed", e);
            throw new CryptoException("Failed to decrypt data", e);
        }
    }

    @Override
    public String hash(String data) {
        if (data == null || data.isEmpty()) {
            return null;
        }

        try {
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (Exception e) {
            log.error("Hashing failed", e);
            throw new CryptoException("Failed to hash data", e);
        }
    }

    @Override
    public boolean verifyHash(String data, String hash) {
        if (data == null || hash == null) {
            return false;
        }
        String computedHash = hash(data);
        return computedHash.equals(hash);
    }

    @Override
    public EncryptedData encryptWithHash(String plaintext) {
        if (plaintext == null || plaintext.isEmpty()) {
            return new EncryptedData(plaintext, null);
        }
        
        String encrypted = encrypt(plaintext);
        String hashed = hash(plaintext);
        return new EncryptedData(encrypted, hashed);
    }

    /**
     * Derives a fixed-length AES key from the configured encryption key using SHA-256
     */
    private SecretKey deriveKey(String key) throws Exception {
        MessageDigest sha = MessageDigest.getInstance(HASH_ALGORITHM);
        byte[] keyBytes = sha.digest(key.getBytes(StandardCharsets.UTF_8));
        byte[] aesKey = new byte[KEY_SIZE];
        System.arraycopy(keyBytes, 0, aesKey, 0, KEY_SIZE);
        return new SecretKeySpec(aesKey, KEY_ALGORITHM);
    }
}
