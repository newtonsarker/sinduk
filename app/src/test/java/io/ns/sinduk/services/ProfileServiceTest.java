package io.ns.sinduk.services;

import io.ns.sinduk.utils.FileUtil;
import io.ns.sinduk.utils.PBEUtil;
import io.ns.sinduk.vo.Profile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class ProfileServiceTest {

    private ProfileService profileService = new ProfileService();

    @BeforeEach
    public void setup() {
        //FileUtil.deleteFile(profileService.publicKeyFilePath());
        //FileUtil.deleteFile(profileService.privateKeyFilePath());
        //FileUtil.deleteFile(profileService.getProfileFilePath());
    }

    @Test
    public void should_return_false_if_profile_does_not_exist() {
        Assertions.assertFalse(profileService.profileExists());
    }

    @Test
    public void should_be_able_to_create_a_new_profile() throws IOException, GeneralSecurityException {
        // given
        var password = "tango";

        var profile = new Profile();
        profile.setFullName("John Doe");
        profile.setEmail("john.doe@sinduk.io");
        profile.setPassword("johndoe");

        // when
        profileService.createProfile(profile, password);

        // then
        Assertions.assertTrue(FileUtil.fileExists(profileService.getProfileFilePath()));

        var encryptedProfile = FileUtil.readFromFile(profileService.getProfileFilePath());
        System.out.println(encryptedProfile);

        var decryptedProfile = PBEUtil.decrypt(encryptedProfile, password);
        System.out.println(decryptedProfile);
    }

}
