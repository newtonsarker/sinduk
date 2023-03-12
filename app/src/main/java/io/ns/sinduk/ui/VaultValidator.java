package io.ns.sinduk.ui;

import io.ns.sinduk.services.ProfileService;
import io.ns.sinduk.vo.PrivateProfile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.concurrent.Callable;

public interface VaultValidator extends Callable<Integer> {

    ProfileService getProfileService();

    String getPassword();

    default boolean isProfileValid() {
        try {
            if (getProfileService().profileExists()) {
                PrivateProfile profile = getProfileService().loadProfile(getPassword());
                if (profile.getProfileId() != null && !profile.getProfileId().isEmpty()) {
                    return Boolean.TRUE;
                }
                System.out.println("Invalid password!");
                return Boolean.FALSE;
            }
            System.out.println("Profile not found!");
            return Boolean.FALSE;
        } catch (IOException | GeneralSecurityException exception) {
            System.out.println("Invalid profile!");
            return Boolean.FALSE;
        }
    }

}
