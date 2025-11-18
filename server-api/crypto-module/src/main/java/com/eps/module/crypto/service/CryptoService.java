package com.eps.module.crypto.service;

/**
 * Generic service interface for encryption, decryption, and hashing operations.
 * Can be used across any project for securing sensitive data.
 */
public interface CryptoService {

    /**
     * Encrypts the given plaintext data using AES-256-GCM encryption
     *
     * @param plaintext The data to encrypt
     * @return Base64 encoded encrypted data with IV prepended
     * @throws com.eps.module.crypto.exception.CryptoException if encryption fails
     */
    String encrypt(String plaintext);

    /**
     * Decrypts the given encrypted data
     *
     * @param encryptedData Base64 encoded encrypted data with IV prepended
     * @return The decrypted plaintext
     * @throws com.eps.module.crypto.exception.CryptoException if decryption fails
     */
    String decrypt(String encryptedData);

    /**
     * Checks if the given data appears to be encrypted (Base64 encoded with sufficient length)
     *
     * @param data The data to check
     * @return true if the data appears to be encrypted, false otherwise
     */
    boolean isEncrypted(String data);

    /**
     * Generates a secure hash (SHA-256) of the given data.
     * This is useful for searching/indexing encrypted data without exposing the original value.
     *
     * @param data The data to hash
     * @return Hex-encoded hash of the data
     * @throws com.eps.module.crypto.exception.CryptoException if hashing fails
     */
    String hash(String data);

    /**
     * Verifies if the given data matches the provided hash
     *
     * @param data The plaintext data
     * @param hash The hash to compare against
     * @return true if the hash matches, false otherwise
     */
    boolean verifyHash(String data, String hash);

    /**
     * Encrypts data and returns both encrypted value and its hash
     *
     * @param plaintext The data to encrypt
     * @return EncryptedData object containing both encrypted value and hash
     */
    EncryptedData encryptWithHash(String plaintext);

    /**
     * Data class to hold both encrypted value and its hash
     */
    record EncryptedData(String encryptedValue, String hash) {}
}
