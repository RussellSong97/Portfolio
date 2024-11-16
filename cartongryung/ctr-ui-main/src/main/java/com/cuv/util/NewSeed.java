package com.cuv.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;

public class NewSeed {
    public static void main(String[] args) throws Exception {
        String carnum = "08오5060";

        byte[] keyBytes = "1234567890123456".getBytes("UTF-8");
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "SEED");

        byte[] plainBytes = carnum.getBytes("UTF-8");

        byte[] paddedPlainBytes = pad(plainBytes);

        Cipher cipher = Cipher.getInstance("SEED/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encryptedBytes = cipher.doFinal(paddedPlainBytes);

        System.out.println("Ciphertext: " + bytesToHex(encryptedBytes));

        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        byte[] unpaddedDecryptedBytes = unpad(decryptedBytes);
        String decryptedText = new String(unpaddedDecryptedBytes, "UTF-8");

        System.out.println("Plaintext: " + decryptedText);
    }

    private static byte[] pad(byte[] bytes) {
        int paddingLength = 16 - (bytes.length % 16);
        byte[] paddedBytes = Arrays.copyOf(bytes, bytes.length + paddingLength);
        for (int i = bytes.length; i < paddedBytes.length; i++) {
            paddedBytes[i] = (byte) paddingLength;
        }
        return paddedBytes;
    }

    private static byte[] unpad(byte[] bytes) {
        int paddingLength = bytes[bytes.length - 1];
        return Arrays.copyOf(bytes, bytes.length - paddingLength);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
