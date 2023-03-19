package io.ns.sinduk.services;

import io.ns.sinduk.vo.PrivateProfile;
import io.ns.sinduk.vo.PublicProfile;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface ProfileService {

    String systemHomeDirectory = "user.home";
    String applicationHone = ".sinduk";
    String profileFileName = "profile.dat";

    /**
     * A location to store application data
     *
     * @return profile location
     */
    String getProfileLocation();

    /**
     * Fully qualified file name of the profile
     *
     * @return path of the profile file
     */
    String getProfileFilePath();

    /**
     * Verifies whether the file exists of not
     *
     * @return true if the file exists, otherwise false
     */
    boolean profileExists();

    /**
     * Creates a new profile with public/private keys and writes the content using password based algorithm
     * Also see {@link ProfileService#writeProfileToFile(PrivateProfile, String)}
     *
     * @param privateProfile The profile to be created
     * @param password The password to encrypt the profile
     * @throws IOException If there is an error during file read/write operation
     * @throws GeneralSecurityException If there is an error during encryption
     */
    void createProfile(PrivateProfile privateProfile, String password) throws IOException, GeneralSecurityException;

    /**
     * Retrieves the public profile
     *
     * @param password The password to unlock profile
     * @return Public profile information
     * @throws GeneralSecurityException If there is an error during encryption
     * @throws IOException If there is an error during file read operation
     */
    PublicProfile retrievePublicProfile(String password) throws GeneralSecurityException, IOException;

    /**
     * Encrypts the given profile using password based encryption and write in the file
     *
     * @param privateProfile The profile to be written in the file
     * @param password The password to encrypt the profile
     * @throws IOException If there is an error during file read/write operation
     * @throws GeneralSecurityException If there is an error during encryption
     */
    void writeProfileToFile(PrivateProfile privateProfile, String password) throws IOException, GeneralSecurityException;

    /**
     * Removes the profile file from the system
     */
    void deleteProfile();

    /**
     * Decrypts the content of the file and loads in the memory
     *
     * @param password A password to decrypt the file content
     * @return The decrypted profile
     * @throws IOException If there is an error during file read/write operation
     * @throws GeneralSecurityException If there is an error during encryption
     */
    PrivateProfile loadProfile(String password) throws IOException, GeneralSecurityException;

}
