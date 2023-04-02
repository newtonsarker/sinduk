package io.ns.sinduk.services;

import io.ns.sinduk.vo.SecretRecord;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface SecretService {

    /**
     * Find a secret record in the record map of the profile
     *
     * @param recordId A record id to look up a secret from the vault
     * @return A record if a secret exist in the vault, or null if it does not exist
     */
    SecretRecord find(String recordId);

    /**
     * Add or update a record in the record map of the profile
     *
     * @param record A record to be added or updated
     * @param profilePassword Profile password to encrypt the profile
     */
    void addOrUpdate(SecretRecord record, String profilePassword) throws GeneralSecurityException, IOException;

    /**
     * Remove a record from the record map in the profile
     *
     * @param recordId A record id to be removed from the record map
     */
    void delete(String recordId);

}
