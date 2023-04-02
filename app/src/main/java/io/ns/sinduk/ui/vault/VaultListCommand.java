package io.ns.sinduk.ui.vault;

import io.ns.sinduk.AppContext;
import io.ns.sinduk.services.ProfileService;
import io.ns.sinduk.ui.Labels;
import io.ns.sinduk.ui.VaultValidator;
import io.ns.sinduk.utils.ConsoleUtil;
import io.ns.sinduk.vo.SecretRecord;
import picocli.CommandLine;

import java.io.Console;
import java.util.ArrayList;

@CommandLine.Command(name="vault-list", version = "0.1", mixinStandardHelpOptions = true, requiredOptionMarker = '*')
public class VaultListCommand implements VaultValidator {

    private Console console = System.console();

    @CommandLine.Option(names = {"-p", "--password"}, description = Labels.password)
    char[] password;

    @Override
    public ProfileService getProfileService() {
        return AppContext.getObject(ProfileService.class);
    }

    @Override
    public String getPassword() {
        if (password == null || password.length < 1) {
            password = ConsoleUtil.readRequiredSecret(console, Labels.password);
        }
        return new String(password);
    }

    @Override
    public Integer call() throws Exception {
        if (isProfileValid()) {
            var profile = getProfileService().loadProfile(getPassword());
            if (profile.getProfileId() != null && !profile.getProfileId().isEmpty()) {
                var secrets = new ArrayList<SecretRecord>();
                secrets.add(createHeaderRow());
                profile.getSecrets().forEach((id, record) -> secrets.add(record));
                printSecretInfo(secrets);
            }
            return CommandLine.ExitCode.OK;
        }
        return CommandLine.ExitCode.SOFTWARE;
    }

    private static SecretRecord createHeaderRow() {
        var headerRow = new SecretRecord();
        headerRow.setRecordId(CommandLine.Help.Ansi.AUTO.string("@|bold,green,underline ID|@"));
        headerRow.setUsername(CommandLine.Help.Ansi.AUTO.string("@|bold,green,underline Username|@"));
        headerRow.setGroupName(CommandLine.Help.Ansi.AUTO.string("@|bold,green,underline Group|@"));
        headerRow.setOrganizationCode(CommandLine.Help.Ansi.AUTO.string("@|bold,green,underline Organization|@"));
        headerRow.setEnvironmentName(CommandLine.Help.Ansi.AUTO.string("@|bold,green,underline Environment|@"));
        return headerRow;
    }

    private void printSecretInfo(ArrayList<SecretRecord> secrets) {
        var rowFormatter = "%-40s%-40s%-15s%-15s%-15s\n";
        if (secrets != null && secrets.size() > 0) {
            secrets.forEach(secret -> {
                System.out.format(
                        rowFormatter,
                        secret.getRecordId(),
                        secret.getUsername(),
                        secret.getGroupName(),
                        secret.getOrganizationCode(),
                        secret.getEnvironmentName()
                );
            });
        }
    }
}
