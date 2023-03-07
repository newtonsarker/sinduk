package io.ns.sinduk.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;

public class PBEUtil {

    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String KEY_DERIVATION_ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final int KEY_LENGTH = 256;
    private static final int ITERATIONS = 65536;
    private static final int SALT_LENGTH = 8;

    public static String encrypt(String plaintext, String password) throws GeneralSecurityException {
        byte[] salt = generateSalt();

        SecretKey secret = deriveKey(password, salt);

        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        AlgorithmParameters params = cipher.getParameters();
        byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
        byte[] ciphertext = cipher.doFinal(plaintext.getBytes());
        byte[] encrypted = concat(salt, iv, ciphertext);
        return Base64Util.encodeToString(encrypted);
    }

    public static String decrypt(String ciphertext, String password) throws GeneralSecurityException {
        byte[] encrypted = Base64Util.decodeToBytes(ciphertext);
        byte[] salt = new byte[SALT_LENGTH];
        byte[] iv = new byte[16];
        byte[] ciphertextBytes = new byte[encrypted.length - SALT_LENGTH - iv.length];
        split(encrypted, salt, iv, ciphertextBytes);

        SecretKey secret = deriveKey(password, salt);

        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
        byte[] plaintextBytes = cipher.doFinal(ciphertextBytes);
        return new String(plaintextBytes);
    }

    private static SecretKey deriveKey(String password, byte[] salt) throws GeneralSecurityException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_DERIVATION_ALGORITHM);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
        SecretKey tmp = factory.generateSecret(spec);
        return new SecretKeySpec(tmp.getEncoded(), "AES");
    }

    private static byte[] concat(byte[]... arrays) {
        int totalLength = 0;
        for (byte[] array : arrays) {
            totalLength += array.length;
        }
        byte[] result = new byte[totalLength];
        int destPos = 0;
        for (byte[] array : arrays) {
            System.arraycopy(array, 0, result, destPos, array.length);
            destPos += array.length;
        }
        return result;
    }

    private static void split(byte[] input, byte[]... arrays) {
        int srcPos = 0;
        for (byte[] array : arrays) {
            System.arraycopy(input, srcPos, array, 0, array.length);
            srcPos += array.length;
        }
    }

    private static byte[] generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        return salt;
    }

}
