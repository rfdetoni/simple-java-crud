package com.test.simplecrud.utils;


import com.test.simplecrud.exceptions.CypherException;
import com.test.simplecrud.exceptions.Errors;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class CryptoUtils {
    private static Cipher cipher;

    static {
        try {
            cipher = Cipher.getInstance("AES");
        } catch ( Exception e) {
            throw new CypherException(Errors.CYPHER_ERROR);
        }
    }

    public static String encryptData(String plainText, String key) {
        try{
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
            byte[] plainTextByte = plainText.getBytes();
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedByte = cipher.doFinal(plainTextByte);
            Base64.Encoder encoder = Base64.getEncoder();
            return encoder.encodeToString(encryptedByte);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new CypherException(Errors.ENCRYPTION_ERROR);
        }
    }

    public static String generateAESKey()  {
        try{
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128);
            SecretKey secretKey = keyGen.generateKey();
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        }catch (NoSuchAlgorithmException e){
            throw new CypherException(Errors.ENCRYPTION_ERROR);
        }
    }

    public static String decryptData(String encryptedText, String key) throws CypherException {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] encryptedTextByte = decoder.decode(encryptedText);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedByte = cipher.doFinal(encryptedTextByte);
            return new String(decryptedByte);
        }catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new CypherException(Errors.DECRYPTION_ERROR);
        }
    }
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private CryptoUtils(){}

    public static String encryptPassword(String password){
        return encoder.encode( password );
    }

    public static boolean validate(String password, String matcher){
        return encoder.matches( password, matcher );
    }
}