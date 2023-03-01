package io.ns.sinduk.services;

import io.ns.sinduk.utils.FileUtil;
import io.ns.sinduk.vo.Profile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class ProfileServiceTest {

    private ProfileService profileService = new ProfileService();

    @BeforeEach
    public void setup() {
        FileUtil.deleteFile(profileService.publicKeyFilePath());
        FileUtil.deleteFile(profileService.privateKeyFilePath());
        FileUtil.deleteFile(profileService.getProfileFilePath());
    }

    @Test
    public void should_return_false_if_profile_does_not_exist() {
        Assertions.assertFalse(profileService.profileExists());
    }

    @Test
    public void should_be_able_to_create_a_new_profile() throws IOException, NoSuchAlgorithmException {
        var profile = new Profile();
        profile.setFullName("John Doe");
        profile.setEmail("john.doe@sinduk.io");
        profile.setPassword("johndoe");

        profileService.createProfile(profile);

        Assertions.assertTrue(FileUtil.fileExists(profileService.publicKeyFilePath()));
        Assertions.assertTrue(FileUtil.fileExists(profileService.privateKeyFilePath()));
        Assertions.assertTrue(FileUtil.fileExists(profileService.getProfileFilePath()));
    }

}
