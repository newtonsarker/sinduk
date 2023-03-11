package io.ns.sinduk.services;

import io.ns.sinduk.utils.FileUtil;
import io.ns.sinduk.vo.PrivateProfile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class ProfileServiceTest {

    private final String profileName = "John Doe";
    private final String profileEmail = "john.doe@sinduk.io";
    private final String profilePassword = "tango";

    private ProfileService profileService = new TestProfileService();

    @Test
    public void should_return_false_if_profile_does_not_exist() {
        FileUtil.deleteFile(profileService.getProfileFilePath());
        Assertions.assertFalse(profileService.profileExists());
    }

    @Test
    public void should_be_able_to_create_a_new_profile() throws IOException, GeneralSecurityException {
        // when
        createNewProfile(profilePassword);

        // then
        Assertions.assertTrue(FileUtil.fileExists(profileService.getProfileFilePath()));
    }

    @Test
    public void should_be_able_to_decrypt_profile() throws GeneralSecurityException, IOException {
        // given
        createNewProfile(profilePassword);

        // when
        var profile = profileService.loadProfile(profilePassword);

        // then
        Assertions.assertNotNull(profile.getProfileId());
        Assertions.assertEquals(profileName, profile.getFullName());
        Assertions.assertEquals(profileEmail, profile.getFullName());
        Assertions.assertNotNull(profile.getPublicKey());
        Assertions.assertNotNull(profile.getPrivateKey());
    }

    private void createNewProfile(String password) throws IOException, GeneralSecurityException {
        profileService.deleteProfile();

        var profile = new PrivateProfile();
        profile.setFullName(profileName);
        profile.setEmail(profileEmail);

        profileService.createProfile(profile, password);
    }

}
