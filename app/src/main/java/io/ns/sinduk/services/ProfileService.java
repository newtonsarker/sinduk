package io.ns.sinduk.services;

import io.ns.sinduk.vo.PrivateProfile;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface ProfileService {

    String systemHomeDirectory = "user.home";
    String applicationHone = ".sinduk";
    String profileFileName = "profile.dat";

    String getProfileLocation();

    String getProfileFilePath();

    boolean profileExists();

    void createProfile(PrivateProfile privateProfile, String password) throws IOException, GeneralSecurityException;

    void deleteProfile();

    PrivateProfile loadProfile(String password) throws IOException, GeneralSecurityException;

}
