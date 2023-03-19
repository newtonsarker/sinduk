package io.ns.sinduk.ui.profile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.ns.sinduk.AppContext;
import io.ns.sinduk.services.ProfileService;
import io.ns.sinduk.ui.Labels;
import io.ns.sinduk.ui.VaultValidator;
import picocli.CommandLine;

@CommandLine.Command(name="profile-view", version = "0.1", mixinStandardHelpOptions = true, requiredOptionMarker = '*')
public class ProfileViewCommand implements VaultValidator {

    @CommandLine.Option(names = {"-p", "--password"}, description = Labels.password)
    char[] password;

    @CommandLine.Option(names = {"-b", "--beautify"}, description = Labels.beautify)
    boolean beautify = false;

    @Override
    public ProfileService getProfileService() {
        return AppContext.getObject(ProfileService.class);
    }

    @Override
    public String getPassword() {
        if (password != null && password.length > 0) {
            return new String(password);
        }
        return null;
    }

    @Override
    public Integer call() throws Exception {
        if (password == null) {
            password = System.console().readPassword(Labels.password + ": ");
        }

        if (!isProfileValid()) {
            return CommandLine.ExitCode.SOFTWARE;
        }

        try {
            var publicProfile = getProfileService().retrievePublicProfile(getPassword());
            var gson = createGsonInstance();
            System.out.println(gson.toJson(publicProfile));
        } catch (Exception exception) {
            System.out.println("Failed to load profile!");
        }

        return CommandLine.ExitCode.SOFTWARE;
    }

    private Gson createGsonInstance() {
        if (beautify) {
            return new GsonBuilder().setPrettyPrinting().create();
        }
        return new Gson();
    }

}
