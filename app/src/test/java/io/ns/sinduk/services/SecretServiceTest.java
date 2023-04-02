package io.ns.sinduk.services;

import io.ns.sinduk.vo.SecretRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

public class SecretServiceTest extends BaseTestService {

    @Disabled
    @Test
    public void should_be_able_to_create_a_new_secret_record() throws GeneralSecurityException, IOException {
        // when
        var profileService = new TestProfileService();
        createNewProfile(profileService);
        var secretService = new SecretServiceImpl(profileService.loadProfile(profilePassword));
        var recordPassword = "test_password_";
        for (int i = 0; i < 2; i++) {
            var record = createRecord(secretService.getProfile().getPublicKey(), recordPassword + i);
            secretService.addOrUpdate(record, profilePassword);
        }

        // then
        var index = 0;
        for (Map.Entry<String, SecretRecord> entry : secretService.getProfile().getSecrets().entrySet()) {
            Assertions.assertEquals(
                    recordPassword + index, entry.getValue().getDecryptedPassword(secretService.getProfile().getPrivateKey())
            );
            index++;
        }
    }

    @Disabled
    @Test
    public void should_perform_consistent_record_manipulation_within_multiple_file_read_write_operation() throws GeneralSecurityException, IOException {
        // given a profile is created
        var profileService = new TestProfileService();
        createNewProfile(profileService);
        var secretService = new SecretServiceImpl(profileService.loadProfile(profilePassword));
        var recordPassword = "test_password_";

        // when 2 records are written in profile file
        for (int i = 0; i < 2; i++) {
            var record = createRecord(secretService.getProfile().getPublicKey(), recordPassword + i);
            secretService.addOrUpdate(record, profilePassword);
        }
        profileService.writeProfileToFile(secretService.getProfile(), profilePassword);

        // then loaded profile should contain 2 records and should be able to decrypt the password
        secretService = new SecretServiceImpl(profileService.loadProfile(profilePassword));
        Assertions.assertEquals(2, secretService.getProfile().getSecrets().size());
        var index = 0;
        for (Map.Entry<String, SecretRecord> entry : secretService.getProfile().getSecrets().entrySet()) {
            Assertions.assertEquals(
                    recordPassword + index, entry.getValue().getDecryptedPassword(secretService.getProfile().getPrivateKey())
            );
            index++;
        }

        // when another 2 records are written in profile file
        for (int i = 2; i < 4; i++) {
            var record = createRecord(secretService.getProfile().getPublicKey(), recordPassword + i);
            secretService.addOrUpdate(record, profilePassword);
        }
        profileService.writeProfileToFile(secretService.getProfile(), profilePassword);

        // then loaded profile should contain 4 records and should be able to decrypt the password
        secretService = new SecretServiceImpl(profileService.loadProfile(profilePassword));
        Assertions.assertEquals(4, secretService.getProfile().getSecrets().size());
        index = 0;
        for (Map.Entry<String, SecretRecord> entry : secretService.getProfile().getSecrets().entrySet()) {
            Assertions.assertEquals(
                    recordPassword + index, entry.getValue().getDecryptedPassword(secretService.getProfile().getPrivateKey())
            );
            index++;
        }
    }

}
