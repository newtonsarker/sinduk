package io.ns.sinduk.ui;

import io.ns.sinduk.AppContext;
import io.ns.sinduk.services.ProfileService;
import io.ns.sinduk.vo.PrivateProfile;
import picocli.CommandLine;

@CommandLine.Command(name="create", version = "0.1", mixinStandardHelpOptions = true, requiredOptionMarker = '*')
public class VaultCreateCommand implements VaultValidator  {

    private static final String nameFieldDescription = "Full name of the user";
    private static final String emailFieldDescription = "Email address of the user";
    private static final String passwordFieldDescription = "Passphrase for sinduk";

    @CommandLine.Option(names = {"-n", "--name"}, description = nameFieldDescription)
    private String fullName;

    @CommandLine.Option(names = {"-e", "--email"}, description = emailFieldDescription)
    private String email;

    @CommandLine.Option(names = {"-p", "--password"}, description = passwordFieldDescription)
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
            fullName = console.readLine(nameFieldDescription + ": ");
        }

        if (email == null) {
            email = console.readLine(emailFieldDescription + ": ");
        }

        if (password == null) {
            password = console.readPassword(passwordFieldDescription + ": ");
        }

        System.out.println("Creating a new profile!");
        var privateProfile = new PrivateProfile();
        privateProfile.setFullName(fullName);
        privateProfile.setEmail(email);
        getProfileService().createProfile(privateProfile, getPassword());

        return CommandLine.ExitCode.SOFTWARE;
    }

}
