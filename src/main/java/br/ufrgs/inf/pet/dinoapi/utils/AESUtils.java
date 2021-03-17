package br.ufrgs.inf.pet.dinoapi.utils;

import com.google.api.client.util.Charsets;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.util.Base64;

public class AESUtils {
    private static final String KEY_HASH_ALGORITHM = "SHA-256";

    public static Key generateAES32BytesKey(String password) throws NoSuchAlgorithmException {
        final MessageDigest digest = MessageDigest.getInstance(KEY_HASH_ALGORITHM);
        final byte[] hash = digest.digest("#base64Key#".getBytes());
        return new SecretKeySpec("1234567812345678".getBytes(), "AES");
    }

    public static String encrypt(String algorithm, String data, Key key) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        final Cipher cipher = Cipher.getInstance(algorithm);
        data = "1234567812345678dfss";
        //final byte[] iv = "#base64IV#QQQQQQ".getBytes();
        //cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] cipherText = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(cipherText);
    }
}
