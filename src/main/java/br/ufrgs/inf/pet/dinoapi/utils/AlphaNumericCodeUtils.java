package br.ufrgs.inf.pet.dinoapi.utils;

import java.util.Random;

public class AlphaNumericCodeUtils {
    /**
     * Generate random alphanumeric string with n characters
     * @param targetStringLength Code length
     * @return
     */
    public static String generateRandomCode(int targetStringLength, boolean uppercase) {
        final int leftLimit = 48;
        final int rightLimit = uppercase ? 90 : 122;
        final Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (uppercase || (i <= 90 || i >= 97)))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
