package io.ns.sinduk.ui;

import io.ns.sinduk.AppContext;
import io.ns.sinduk.services.ProfileService;
import picocli.CommandLine;

@CommandLine.Command(
        name="list",
        version = "0.1",
        mixinStandardHelpOptions = true,
        requiredOptionMarker = '*'
)
public class VaultListCommand implements VaultValidator  {

    @CommandLine.Option(names = {"-p", "--password"}, required = true, interactive = true, description = "Passphrase for sinduk.")
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
        if (isProfileValid()) {
            var profile = getProfileService().loadProfile(getPassword());
            if (profile.getProfileId() != null && !profile.getProfileId().isEmpty()) {
                String recordId = CommandLine.Help.Ansi.AUTO.string("@|bold,green,underline ID|@");
                String username = CommandLine.Help.Ansi.AUTO.string("@|bold,green,underline Username|@");
                String groupName = CommandLine.Help.Ansi.AUTO.string("@|bold,green,underline Group|@");
                String organizationCode = CommandLine.Help.Ansi.AUTO.string("@|bold,green,underline Organization|@");
                String environmentName = CommandLine.Help.Ansi.AUTO.string("@|bold,green,underline Environment|@");
                var rowFormatter = "%-15s%-15s%-15s%-15s%-15s\n";
                System.out.format(rowFormatter, recordId, username, groupName, organizationCode, environmentName);
                profile.getSecrets().forEach((id, record) -> {
                    System.out.format(
                            rowFormatter,
                            record.getRecordId(),
                            record.getUsername(),
                            record.getGroupName(),
                            record.getOrganizationCode(),
                            record.getEnvironmentName()
                    );
                });
            }
        }
        return CommandLine.ExitCode.SOFTWARE;
    }

}
