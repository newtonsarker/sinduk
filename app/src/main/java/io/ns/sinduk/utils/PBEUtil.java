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

/**
 * A utility class that provides methods for encrypting and decrypting data using a symmetric key.
 * Uses AES encryption algorithm in CBC mode with PKCS5Padding.
 */
public class PBEUtil {

    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String KEY_DERIVATION_ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final int KEY_LENGTH = 256;
    private static final int ITERATIONS = 65536;
    private static final int SALT_LENGTH = 8;

    /**
     * Encrypts a given string using the specified key
     *
     * @param plaintext The plaintext string to encrypt
     * @param password The password to encrypt the text with
     * @return The base64-encoded ciphertext
     * @throws GeneralSecurityException If there is an error during encryption
     */
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

    /**
     * Decrypts a given ciphertext string using the specified key
     *
     * @param ciphertext The base64-encoded ciphertext to decrypt
     * @param password The password to decrypt the text with
     * @return The decrypted text
     * @throws GeneralSecurityException
     */
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

    /**
     * Derives a symmetric key from a given password and salt using PBKDF2 algorithm
     *
     * @param password The password to derive the key from
     * @param salt The salt to use in the derivation process
     * @return The derived SecretKey object
     * @throws GeneralSecurityException If there is an error during SecretKey generation
     */
    private static SecretKey deriveKey(String password, byte[] salt) throws GeneralSecurityException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_DERIVATION_ALGORITHM);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
        SecretKey tmp = factory.generateSecret(spec);
        return new SecretKeySpec(tmp.getEncoded(), "AES");
    }

    /**
     * Concatenates given byte arrays into a single byte array
     *
     * @param arrays Arrays of byte array
     * @return The concatenated byte array
     */
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

    /**
     * Distribute a single byte array to multiple byte arrays
     *
     * @param input The source byte array to be distributed
     * @param arrays The array of byte arrays to distribute the source byte array
     */
    private static void split(byte[] input, byte[]... arrays) {
        int srcPos = 0;
        for (byte[] array : arrays) {
            System.arraycopy(input, srcPos, array, 0, array.length);
            srcPos += array.length;
        }
    }

    /**
     * Generate the salt to use in the derivation process
     *
     * @return the salt
     */
    private static byte[] generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        return salt;
    }

}
