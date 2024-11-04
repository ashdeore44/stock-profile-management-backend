package com.spm.config;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class SecurityConfig {

    private static final String AES_ALGORITHM = "AES/ECB/PKCS5Padding";
    private static final String CHARSET_NAME = "UTF-8";
    private static final int AES_KEY_SIZE = 128;

    private static String secretKey = "1234567890123456";

    public static String encrypt(String plainPassword) throws Exception {
        SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(CHARSET_NAME), "AES");
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(plainPassword.getBytes(CHARSET_NAME));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }
}