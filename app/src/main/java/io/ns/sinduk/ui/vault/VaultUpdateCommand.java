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

@CommandLine.Command(name="vault-update", version = "0.1", mixinStandardHelpOptions = true, requiredOptionMarker = '*')
public class VaultUpdateCommand implements VaultValidator {

    private final Console console = System.console();

    @CommandLine.Option(names = {"-p", "--password"}, description = Labels.password)
    char[] password;

    @CommandLine.Option(names = {"-i", "--identifier"}, description = Labels.secretId)
    String id;

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

    public String getId() {
        if (id == null || id.isEmpty()) id = ConsoleUtil.readRequiredParameter(console, Labels.secretId);
        return id;
    }

    @Override
    public Integer call() throws Exception {
        if (isProfileValid()) {
            var profile = getProfileService().loadProfile(getPassword());
            var secretRecord = profile.getSecrets().get(getId());
            if (secretRecord != null) {
                readParameters(secretRecord, profile.getPrivateKey());
                updateRecord(secretRecord, profile.getPublicKey());
                new SecretServiceImpl(profile).addOrUpdate(secretRecord,getPassword());
                return CommandLine.ExitCode.OK;
            }
        }
        return CommandLine.ExitCode.SOFTWARE;
    }

    private void readParameters(SecretRecord secretRecord, String privateKey) throws GeneralSecurityException {
        username = ConsoleUtil.readParameterOrDefault(console, Labels.username, username, secretRecord.getUsername());
        secret = ConsoleUtil.readSecretOrDefault(console,
                Labels.secret,
                secret,
                secretRecord.getDecryptedPassword(privateKey).toCharArray());
        groupName = ConsoleUtil.readParameterOrDefault(console,
                Labels.groupName,
                groupName,
                secretRecord.getGroupName());
        organizationCode = ConsoleUtil.readParameterOrDefault(console,
                Labels.organization,
                organizationCode,
                secretRecord.getOrganizationCode());
        environmentName = ConsoleUtil.readParameterOrDefault(console,
                Labels.environment,
                environmentName,
                secretRecord.getEnvironmentName());
        note = ConsoleUtil.readParameterOrDefault(console, Labels.note, note, secretRecord.getNote());
    }

    private void updateRecord(SecretRecord record, String publicKey) throws GeneralSecurityException {
        record.setUsername(username);
        record.setPassword(publicKey, new String(secret));
        record.setGroupName(groupName);
        record.setOrganizationCode(organizationCode);
        record.setEnvironmentName(environmentName);
        record.setNote(note);
    }

}
