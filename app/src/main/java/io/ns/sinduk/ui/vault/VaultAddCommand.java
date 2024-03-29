package io.ns.sinduk.ui.vault;

import io.ns.sinduk.AppContext;
import io.ns.sinduk.services.ProfileService;
import io.ns.sinduk.services.SecretServiceImpl;
import io.ns.sinduk.ui.Labels;
import io.ns.sinduk.ui.VaultValidator;
import io.ns.sinduk.utils.ConsoleUtil;
import io.ns.sinduk.vo.SecretRecord;
import picocli.CommandLine;

import java.io.Console;
import java.security.GeneralSecurityException;
import java.util.UUID;

@CommandLine.Command(name="vault-add", version = "0.1", mixinStandardHelpOptions = true, requiredOptionMarker = '*')
public class VaultAddCommand implements VaultValidator {

    private final Console console = System.console();

    @CommandLine.Option(names = {"-p", "--password"}, description = Labels.password)
    char[] password;

    @CommandLine.Option(names = {"-u", "--username"}, description = Labels.username)
    String username;

    @CommandLine.Option(names = {"-s", "--secret"}, description = Labels.secret)
    char[] secret;

    @CommandLine.Option(names = {"-g", "--group"}, description = Labels.groupName)
    String groupName;

    @CommandLine.Option(names = {"-o", "--organization"}, description = Labels.organization)
    String organizationCode;

    @CommandLine.Option(names = {"-e", "--environment"}, description = Labels.environment)
    String environmentName;

    @CommandLine.Option(names = {"-n", "--note"}, description = Labels.note)
    String note;

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
            if (!isProfileValid()) return CommandLine.ExitCode.SOFTWARE;
            readParameters();

            var profile = getProfileService().loadProfile(getPassword());
            var publicKey = profile.getPublicKey();
            new SecretServiceImpl(profile).addOrUpdate(createRecord(publicKey), getPassword());

            return CommandLine.ExitCode.OK;
        }
        return CommandLine.ExitCode.SOFTWARE;
    }

    private void readParameters() {
        if (username == null) username = ConsoleUtil.readRequiredParameter(console, Labels.username);
        if (secret == null) secret = ConsoleUtil.readRequiredSecret(console, Labels.secret);
        groupName = ConsoleUtil.readOptionalParameter(console, Labels.groupName, groupName);
        organizationCode = ConsoleUtil.readOptionalParameter(console, Labels.organization, organizationCode);
        environmentName = ConsoleUtil.readOptionalParameter(console, Labels.environment, environmentName);
        note = ConsoleUtil.readOptionalParameter(console, Labels.note, note);
    }

    SecretRecord createRecord(String publicKey) throws GeneralSecurityException {
        var record = new SecretRecord();
        record.setRecordId(UUID.randomUUID().toString());
        record.setUsername(username);
        record.setPassword(publicKey, new String(secret));
        record.setGroupName(groupName);
        record.setOrganizationCode(organizationCode);
        record.setEnvironmentName(environmentName);
        record.setNote(note);
        return record;
    }

}
