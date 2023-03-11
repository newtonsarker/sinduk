package io.ns.sinduk.services;

import io.ns.sinduk.utils.PublicKeyUtil;

import java.security.GeneralSecurityException;

public interface PasswordEncrypter {

    String getPassword();

    default String encryptPassword(String publicKey, String password) throws GeneralSecurityException {
        return PublicKeyUtil.encrypt(publicKey, password);
    }

    default String getDecryptedPassword(String privateKey) throws GeneralSecurityException {
        return PublicKeyUtil.decrypt(privateKey, getPassword());
    }

}
