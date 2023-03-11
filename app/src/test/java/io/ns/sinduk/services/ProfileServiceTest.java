package io.ns.sinduk.services;

import io.ns.sinduk.utils.FileUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class ProfileServiceTest extends BaseTestService {

    private ProfileService profileService;

    @BeforeEach
    public void initTest() throws GeneralSecurityException, IOException {
        profileService = new TestProfileService();
        createNewProfile(profileService);
    }

    @Test
    public void should_return_false_if_profile_does_not_exist() {
        FileUtil.deleteFile(profileService.getProfileFilePath());
        Assertions.assertFalse(profileService.profileExists());
    }

    @Test
    public void should_be_able_to_create_a_new_profile() throws IOException, GeneralSecurityException {
        // when a profile is created

        // then
        Assertions.assertTrue(FileUtil.fileExists(profileService.getProfileFilePath()));
    }

    @Test
    public void should_be_able_to_decrypt_profile() throws GeneralSecurityException, IOException {
        // when
        var profile = profileService.loadProfile(profilePassword);

        // then
        Assertions.assertNotNull(profile.getProfileId());
        Assertions.assertEquals(profileName, profile.getFullName());
        Assertions.assertEquals(profileEmail, profile.getEmail());
        Assertions.assertNotNull(profile.getPublicKey());
        Assertions.assertNotNull(profile.getPrivateKey());
    }

}
