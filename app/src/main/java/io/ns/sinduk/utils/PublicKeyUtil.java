package io.ns.sinduk.utils;

import javax.crypto.Cipher;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * This class provides an example of how to encrypt and decrypt a message
 * using public key encryption.
 */
public class PublicKeyUtil {

    private static final String ALGORITHM = "RSA";
    private static final int KEY_SIZE = 2048;

    public static KeyPair generateKeyPair() throws GeneralSecurityException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
        keyGen.initialize(KEY_SIZE, SecureRandom.getInstanceStrong());
        return keyGen.generateKeyPair();
    }

    public static String encrypt(String encodedPublicKey, String message) throws GeneralSecurityException {
        byte[] publicKeyBytes = Base64Util.decodeToBytes(encodedPublicKey);
        PublicKey publicKey = KeyFactory.getInstance(ALGORITHM).generatePublic(new X509EncodedKeySpec(publicKeyBytes));
        byte[] encryptedMessage = createCipher(publicKey, Cipher.ENCRYPT_MODE).doFinal(message.getBytes());
        return Base64Util.encodeToString(encryptedMessage);
    }

    public static String decrypt(String encodedPrivateKey, String encodedEncryptedMessage) throws GeneralSecurityException {
        byte[] privateKeyBytes = Base64Util.decodeToBytes(encodedPrivateKey);
        PrivateKey privateKey = KeyFactory.getInstance(ALGORITHM).generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
        byte[] encryptedMessage = Base64Util.decodeToBytes(encodedEncryptedMessage);
        byte[] decryptedMessage = createCipher(privateKey, Cipher.DECRYPT_MODE).doFinal(encryptedMessage);
        return new String(decryptedMessage);
    }

    private static Cipher createCipher(Key key, int cipherMode) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(cipherMode, key);
        return cipher;
    }

}
