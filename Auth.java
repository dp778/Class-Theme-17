package com.company;
/*
 * 19408
 * удостоверителен алгоритъм за хеширане с AES-256 и salting на пароли и валидация на пароли
 */
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Auth {

    public static byte[] hashPassword(String input, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(salt);

        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }

    public static String toHexString(byte[] hash) // превръщане на хешираната парола в hex String
    {
        BigInteger number = new BigInteger(1, hash);
        StringBuilder hexString = new StringBuilder(number.toString(16));

        while (hexString.length() < 64) { hexString.insert(0, '0'); }

        return hexString.toString();
    }

    public static String toHexString(byte[] hash, boolean forSalt) // превръщане на salt-а в hex String
    {
        BigInteger number = new BigInteger(1, hash);

        return number.toString(16);
    }

    public static byte[] hexToByteArray(String hex) { // hex String към масив от байтове
        int length = hex.length();
        byte[] result = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            result[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i+1), 16));
        }
        return result;
    }

    public static byte[] generateSalt(){ // създаване на salt (случайни 16 байта)
        SecureRandom rand = new SecureRandom();

        byte[] salt = new byte[16];
        rand.nextBytes(salt);

        return salt;
    }

    public static String createHash(String password) throws NoSuchAlgorithmException { // хеширане на парола
        byte[] salt = generateSalt();

        return toHexString(hashPassword(password, salt)) + ":" + toHexString(salt, true);
    }

    public static boolean match(String password, String db_hash, String salt) throws NoSuchAlgorithmException { // проверка на парола
        String hashed_salted = toHexString(hashPassword(password, hexToByteArray(salt)));
        return db_hash.equals(hashed_salted);
    }


    public static void main(String[] args) throws NoSuchAlgorithmException { // тестване с примерна парола
        System.out.println(createHash("gangelov1984"));

        System.out.println("Password match: " + match("gangelov1984", "2601d90b84493de60dcb3d44686d6e117e4f5b5f898cfe34bbff7d33edf7abb7", "f7205618752fed3b61ad11beb4efbfeb"));
    }
}