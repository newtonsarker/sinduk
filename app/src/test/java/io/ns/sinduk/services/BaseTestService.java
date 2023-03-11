package io.ns.sinduk.services;

import io.ns.sinduk.vo.PrivateProfile;
import io.ns.sinduk.vo.SecretRecord;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class BaseTestService {

    static final String profileName = "John Doe";
    static final String profileEmail = "john.doe@sinduk.io";
    static final String profilePassword = "tango";

    static void createNewProfile(ProfileService profileService)
            throws IOException, GeneralSecurityException
    {
        profileService.deleteProfile();

        var profile = new PrivateProfile();
        profile.setFullName(profileName);
        profile.setEmail(profileEmail);

        profileService.createProfile(profile, profilePassword);
    }

    SecretRecord createRecord(String publicKey, String recordPassword) throws GeneralSecurityException {
        var record = new SecretRecord();
        record.setUsername("jane.doe@sinduk.io");
        record.setPassword(publicKey, recordPassword);
        record.setGroupName("HR Systems");
        record.setOrganizationCode("XBJISYGWGR");
        record.setEnvironmentName("dev");
        record.setNote("Passwords should be kept secret: " + recordPassword);
        return record;
    }

}
