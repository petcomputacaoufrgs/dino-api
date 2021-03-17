package br.ufrgs.inf.pet.dinoapi.utils;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

public class AESUtils {
    public static Key generateAES32BytesKey(String password) throws NoSuchAlgorithmException {
        final String hash32Bytes = HashUtils.sha3of256(password).substring(0, 32);
        return new SecretKeySpec(hash32Bytes.getBytes(StandardCharsets.UTF_8), "AES");
    }

    public static String encrypt(String algorithm, String data, Key key) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        final Cipher cipher = Cipher.getInstance(algorithm);
        data = "1234567812345678dfss";
        final byte[] iv = "#base64IV#QQQQQQ".getBytes();
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
        //cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] cipherText = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(cipherText);
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
