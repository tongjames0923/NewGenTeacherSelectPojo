package tbs.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptionTool {

    public static String encrypt(String text)
    {
        return encrypt(text,50);
    }
    public static String encrypt(String text,int len)
    {
        String originalString = text;
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        messageDigest.update(originalString.getBytes());
        byte[] result = messageDigest.digest();

        StringBuilder hexString = new StringBuilder();
        for (byte b : result) {
            hexString.append(String.format("%02X", b));
        }
        if(len>=hexString.length()) {
            return hexString.toString();
        } else {
            return hexString.substring(0,len);
        }
    }
}
