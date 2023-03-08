package io.ns.sinduk.services;

import com.google.gson.Gson;
import io.ns.sinduk.utils.FileUtil;
import io.ns.sinduk.utils.PBEUtil;
import io.ns.sinduk.vo.Profile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class ProfileServiceTest {

    private static final Gson gson = new Gson();

    private ProfileService profileService = new ProfileService();

    @Test
    public void should_return_false_if_profile_does_not_exist() {
        FileUtil.deleteFile(profileService.getProfileFilePath());
        Assertions.assertFalse(profileService.profileExists());
    }

    @Test
    public void should_be_able_to_create_a_new_profile() throws IOException, GeneralSecurityException {
        // when
        var password = "tango";
        createNewProfile(password);

        // then
        Assertions.assertTrue(FileUtil.fileExists(profileService.getProfileFilePath()));
    }

    @Test
    public void should_be_able_to_decrypt_profile() throws GeneralSecurityException, IOException {
        // given
        var password = "tango";
        createNewProfile(password);

        // when
        var encryptedProfile = FileUtil.readFromFile(profileService.getProfileFilePath());
        var decryptedProfile = PBEUtil.decrypt(encryptedProfile, password);

        // then
        var profile = gson.fromJson(decryptedProfile, Profile.class);
        Assertions.assertEquals("John Doe", profile.getFullName());
    }

    private void createNewProfile(String password) throws IOException, GeneralSecurityException {
        FileUtil.deleteFile(profileService.getProfileFilePath());

        var profile = new Profile();
        profile.setFullName("John Doe");
        profile.setEmail("john.doe@sinduk.io");

        profileService.createProfile(profile, password);
    }

}
