package br.ufrgs.inf.pet.dinoapi.utils;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

public class AESUtils {
    private static final String ENCRYPT_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String SECRET_ALGORITHM = "AES";

    public static Key generateAESKey(String key) {
        final String key32Bytes = key.substring(0, 32);
        return new SecretKeySpec(key32Bytes.getBytes(StandardCharsets.UTF_8), SECRET_ALGORITHM);
    }

    /**
     * Encrypt string with AES
     * @param data string to be encrypted
     * @param key string of 32 bytes to generate token
     * @param iv string of 16 bytes to randomize token
     * @return generated token
     */
    public static String encrypt(String data, Key key, String iv) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        final Cipher cipher = Cipher.getInstance(ENCRYPT_ALGORITHM);
        final byte[] ivBytes = iv.getBytes(StandardCharsets.UTF_8);
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(ivBytes));
        byte[] cipherText = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(cipherText);
    }
}
