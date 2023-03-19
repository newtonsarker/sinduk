package io.ns.sinduk.ui.profile;

import io.ns.sinduk.AppContext;
import io.ns.sinduk.services.ProfileService;
import io.ns.sinduk.ui.Labels;
import io.ns.sinduk.ui.VaultValidator;
import io.ns.sinduk.vo.PrivateProfile;
import picocli.CommandLine;

@CommandLine.Command(name="profile-create", version = "0.1", mixinStandardHelpOptions = true, requiredOptionMarker = '*')
public class ProfileCreateCommand implements VaultValidator {

    @CommandLine.Option(names = {"-n", "--name"}, description = Labels.fullName)
    private String fullName;

    @CommandLine.Option(names = {"-e", "--email"}, description = Labels.email)
    private String email;

    @CommandLine.Option(names = {"-p", "--password"}, description = Labels.password)
    char[] password;

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
        if (getProfileService().profileExists()) {
            System.out.println("Profile already exists!");
            return CommandLine.ExitCode.OK;
        }

        var console = System.console();

        if (fullName == null) {
            fullName = console.readLine(Labels.fullName + ": ");
        }

        if (email == null) {
            email = console.readLine(Labels.email + ": ");
        }

        if (password == null) {
            password = console.readPassword(Labels.password + ": ");
        }

        System.out.println("Creating a new profile!");

        try {
            var privateProfile = new PrivateProfile();
            privateProfile.setFullName(fullName);
            privateProfile.setEmail(email);
            getProfileService().createProfile(privateProfile, getPassword());
            return CommandLine.ExitCode.OK;
        } catch (Exception exception) {
            System.out.println("Profile creation failed. Please try again!");
        }

        return CommandLine.ExitCode.SOFTWARE;
    }

}
