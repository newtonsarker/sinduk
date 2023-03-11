package io.ns.sinduk.services;

import io.ns.sinduk.vo.SecretRecord;

public interface SecretService {

    /**
     * Add or update a record in the record map of the profile
     *
     * @param record A record to be added or updated
     */
    void addOrUpdate(SecretRecord record);

    /**
     * Remove a record from the record map in the profile
     *
     * @param recordId A record id to be removed from the record map
     */
    void delete(String recordId);

}
