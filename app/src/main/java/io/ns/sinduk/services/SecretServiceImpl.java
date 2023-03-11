package io.ns.sinduk.services;

import io.ns.sinduk.vo.PrivateProfile;
import io.ns.sinduk.vo.SecretRecord;

import java.util.UUID;

public class SecretServiceImpl implements SecretService {

    private PrivateProfile profile;

    public SecretServiceImpl(PrivateProfile profile) {
        this.profile = profile;
    }

    public PrivateProfile getProfile() {
        return profile;
    }

    @Override
    public void addOrUpdate(SecretRecord record) {
        if (record.getRecordId() == null || record.getRecordId().isEmpty()) {
            record.setRecordId(UUID.randomUUID().toString());
        }
        profile.getSecrets().put(record.getRecordId(), record);
    }

    @Override
    public void delete(String recordId) {
        profile.getSecrets().remove(recordId);
    }

}
