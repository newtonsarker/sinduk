package io.ns.sinduk.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PBEUtilTest {

    @Test
    public void should_be_able_to_encrypt_decrypt_text() throws Exception {
        // given
        var plainText = "Tango and Cash";
        var password = "SkyWalker";

        // when
        var encryptedText = PBEUtil.encrypt(plainText, password);
        var decryptedText = PBEUtil.decrypt(encryptedText, password);

        // then
        Assertions.assertEquals(plainText, decryptedText);
    }

}
