package io.ns.sinduk.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.ns.sinduk.utils.Base64Util;
import io.ns.sinduk.utils.FileUtil;
import io.ns.sinduk.utils.PBEUtil;
import io.ns.sinduk.utils.PublicKeyUtil;
import io.ns.sinduk.vo.PrivateProfile;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.UUID;

public class ProfileServiceImpl implements ProfileService {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public String getProfileLocation() {
        var userHone = System.getProperty(systemHomeDirectory);
        return userHone.concat(File.separator.concat(applicationHone));
    }

    @Override
    public String getProfileFilePath() {
        return getProfileLocation().concat(File.separator).concat(profileFileName);
    }

    @Override
    public boolean profileExists() {
        File file = new File(getProfileFilePath());
        return file.exists();
    }

    @Override
    public void createProfile(PrivateProfile privateProfile, String password) throws IOException, GeneralSecurityException {
        var keyPair = PublicKeyUtil.generateKeyPair();
        privateProfile.setProfileId(UUID.randomUUID().toString());
        privateProfile.setPublicKey(Base64Util.encodeToString(keyPair.getPublic().getEncoded()));
        privateProfile.setPrivateKey(Base64Util.encodeToString(keyPair.getPrivate().getEncoded()));

        var profileJson = gson.toJson(privateProfile);

        FileUtil.deleteFile(getProfileFilePath());
        FileUtil.writeToFile(getProfileFilePath(), PBEUtil.encrypt(profileJson, password));
    }

    @Override
    public void deleteProfile() {
        if (profileExists()) {
            FileUtil.deleteFile(getProfileFilePath());
        }
    }

    @Override
    public PrivateProfile loadProfile(String password) throws IOException, GeneralSecurityException {
        var encryptedProfile = FileUtil.readFromFile(getProfileFilePath());
        var decryptedProfile = PBEUtil.decrypt(encryptedProfile, password);
        return gson.fromJson(decryptedProfile, PrivateProfile.class);
    }

}
