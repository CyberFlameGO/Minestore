package com.lielamar.minestore.shared.encryption;

import javax.annotation.Nullable;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public class AES {

    /**
     * Generates a SecretKeySpec object from the given key
     *
     * @param key   Key to use to generate the SecretKeySpec object
     */
    @Nullable
    public static SecretKey getKeyObject(String key) {
        MessageDigest sha;
        byte[] keyBytes;

        try {
            keyBytes = key.getBytes(StandardCharsets.UTF_8);
            sha = MessageDigest.getInstance("SHA-1");
            keyBytes = sha.digest(keyBytes);
            keyBytes = Arrays.copyOf(keyBytes, 16);
            return new SecretKeySpec(keyBytes, "AES");
        } catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Encrypts a string using a key
     *
     * @param decrypted   Decrypted string to encrypt
     * @param key         Key to use for encryption
     * @return            Encrypted string
     */
    @Nullable
    public static String encrypt(String decrypted, String key) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, getKeyObject(key));
            return Base64.getEncoder().encodeToString(cipher.doFinal(decrypted.getBytes(StandardCharsets.UTF_8)));
        } catch(Exception e) {
            System.out.println("Error while encrypting: " + e.toString());
        }

        return null;
    }

    /**
     * Encrypts a string using a key
     *
     * @param encrypted   Encrypted string to decrypt
     * @param key         Key to use for decryption
     * @return            Decrypted string
     */
    @Nullable
    public static String decrypt(String encrypted, String key) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, getKeyObject(key));
            return new String(cipher.doFinal(Base64.getDecoder().decode(encrypted)));
        } catch(Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        }

        return null;
    }
}