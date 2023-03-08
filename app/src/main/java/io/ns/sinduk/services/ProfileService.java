package io.ns.sinduk.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.ns.sinduk.utils.Base64Util;
import io.ns.sinduk.utils.FileUtil;
import io.ns.sinduk.utils.PBEUtil;
import io.ns.sinduk.utils.PublicKeyUtil;
import io.ns.sinduk.vo.Profile;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class ProfileService {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final String userHone = System.getProperty("user.home");
    private static final String applicationHone = ".sinduk";
    private static final String profileFileName = "profile.dat";
    private static final String publicKeyFileName = "public.key";
    private static final String privateKeyFileName = "private.key";


    public boolean profileExists() {
        File file = new File(getProfileFilePath());
        return file.exists();
    }

    public void createProfile(Profile profile, String password) throws IOException, GeneralSecurityException {
        var keyPair = PublicKeyUtil.generateKeyPair();
        profile.setPublicKey(Base64Util.encodeToString(keyPair.getPublic().getEncoded()));
        profile.setPrivateKey(Base64Util.encodeToString(keyPair.getPrivate().getEncoded()));

        var profileJson = gson.toJson(profile);

        FileUtil.deleteFile(getProfileFilePath());
        FileUtil.writeToFile(getProfileFilePath(), PBEUtil.encrypt(profileJson, password));
    }

    public String getProfileLocation() {
        return userHone.concat(File.separator.concat(applicationHone));
    }

    public String getProfileFilePath() {
        return getProfileLocation().concat(File.separator).concat(profileFileName);
    }

    public String publicKeyFilePath() {
        return getProfileLocation().concat(File.separator).concat(publicKeyFileName);
    }

    public String privateKeyFilePath() {
        return getProfileLocation().concat(File.separator).concat(privateKeyFileName);
    }

}
