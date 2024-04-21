package com.boilerplate.configuration.data;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class AES {
    private static final Logger logger = LogManager.getLogger(AES.class);
    private static final String DEFAULT_SECRET = "PHISHCODESECRETENCRYPTIONHASH";
    public static final String ENCRYPTION_PREFIX = "ENC#";

    private static SecretKeySpec secretKey;

    private AES() {
    }

    public static void setKey(String myKey) {
        MessageDigest sha;
        try {
            byte[] key = myKey.getBytes(StandardCharsets.UTF_8);
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static String encrypt(String strToEncrypt, String secret, Boolean withPrefix) {
        try {
            String prefix = withPrefix ? ENCRYPTION_PREFIX : "";
            setKey(secret != null ? secret : DEFAULT_SECRET);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return prefix + Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            System.out.println("Error while encrypting: " + e);
        }
        return null;
    }

    public static String decrypt(String strToDecrypt, String secret, Boolean withPrefix) {
        try {
            if(withPrefix){
                strToDecrypt = strToDecrypt.replace(ENCRYPTION_PREFIX, "");
            }
            setKey(secret != null ? secret : DEFAULT_SECRET);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
            logger.error("Error while decrypting: " + e);
        }
        return null;
    }
}

