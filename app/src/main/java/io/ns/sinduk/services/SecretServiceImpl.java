package io.ns.sinduk.services;

import io.ns.sinduk.AppContext;
import io.ns.sinduk.vo.PrivateProfile;
import io.ns.sinduk.vo.SecretRecord;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.UUID;

public class SecretServiceImpl implements SecretService {

    private ProfileService profileService = AppContext.getObject(ProfileService.class);
    private PrivateProfile profile;

    public SecretServiceImpl(PrivateProfile profile) {
        this.profile = profile;
    }

    public PrivateProfile getProfile() {
        return profile;
    }

    @Override
    public SecretRecord find(String recordId) {
        return profile.getSecrets().get(recordId);
    }

    @Override
    public void addOrUpdate(SecretRecord record, String profilePassword) throws GeneralSecurityException, IOException {
        if (record.getRecordId() == null || record.getRecordId().isEmpty()) {
            record.setRecordId(UUID.randomUUID().toString());
        }
        profile.getSecrets().put(record.getRecordId(), record);
        profileService.writeProfileToFile(profile, profilePassword);
    }

    @Override
    public void delete(String recordId) {
        profile.getSecrets().remove(recordId);
    }

}
