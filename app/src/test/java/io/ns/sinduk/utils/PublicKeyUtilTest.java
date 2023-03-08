package io.ns.sinduk.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.GeneralSecurityException;
import java.security.KeyPair;

public class PublicKeyUtilTest {

    @Test
    public void should_be_able_to_encrypt_decrypt_text() throws GeneralSecurityException {
        // given
        var plainText = "Tango and Cash";
        KeyPair keyPair = PublicKeyUtil.generateKeyPair();
        String encodedPublicKey = Base64Util.encodeToString(keyPair.getPublic().getEncoded());
        String encodedPrivateKey = Base64Util.encodeToString(keyPair.getPrivate().getEncoded());

        // when
        String encryptedText = PublicKeyUtil.encrypt(encodedPublicKey, plainText);
        String decryptedText = PublicKeyUtil.decrypt(encodedPrivateKey, encryptedText);

        // then
        Assertions.assertEquals(plainText, decryptedText);
    }

}
