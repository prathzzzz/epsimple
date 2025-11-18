package com.eps.module.crypto.util;

import com.eps.module.crypto.service.CryptoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Utility class providing static-like access to CryptoService.
 * Useful for scenarios where dependency injection is not available.
 */
@Component
@RequiredArgsConstructor
public class CryptoUtil {

    private static CryptoService cryptoService;

    private final CryptoService service;

    @jakarta.annotation.PostConstruct
    private void init() {
        cryptoService = service;
    }

    public static String encrypt(String plaintext) {
        return cryptoService.encrypt(plaintext);
    }

    public static String decrypt(String encryptedData) {
        return cryptoService.decrypt(encryptedData);
    }

    public static String hash(String data) {
        return cryptoService.hash(data);
    }

    public static boolean verifyHash(String data, String hash) {
        return cryptoService.verifyHash(data, hash);
    }

    public static CryptoService.EncryptedData encryptWithHash(String plaintext) {
        return cryptoService.encryptWithHash(plaintext);
    }
}
