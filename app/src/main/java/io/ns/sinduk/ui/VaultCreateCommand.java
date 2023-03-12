package io.ns.sinduk.ui;

import io.ns.sinduk.AppContext;
import io.ns.sinduk.services.ProfileService;
import picocli.CommandLine;

@CommandLine.Command(name="create", version = "0.1", mixinStandardHelpOptions = true, requiredOptionMarker = '*')
public class VaultCreateCommand implements VaultValidator  {

    @CommandLine.Option(names = {"-n", "--name"}, required = true, interactive = true, description = "Full name of the user.")
    private String fullName;

    @CommandLine.Option(names = {"-e", "--email"}, required = true, interactive = true, description = "Email address of the user.")
    private String email;

    @CommandLine.Option(names = {"-p", "--password"}, required = true, interactive = true, description = "Passphrase for sinduk.")
    String password;

    @Override
    public ProfileService getProfileService() {
        return AppContext.getObject(ProfileService.class);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Integer call() throws Exception {
        System.out.println("fullName: " + fullName);
        System.out.println("email: " + email);
        System.out.println("password: " + password);

        if (isProfileValid()) {
            System.out.println("Profile already exists!");
            return CommandLine.ExitCode.OK;
        }
        return CommandLine.ExitCode.SOFTWARE;
    }

}
