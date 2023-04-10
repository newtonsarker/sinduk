package io.ns.sinduk.ui.vault;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.ns.sinduk.AppContext;
import io.ns.sinduk.services.ProfileService;
import io.ns.sinduk.ui.Labels;
import io.ns.sinduk.ui.VaultValidator;
import io.ns.sinduk.utils.ConsoleUtil;
import io.ns.sinduk.vo.PrivateProfile;
import io.ns.sinduk.vo.SecretInfo;
import io.ns.sinduk.vo.SecretRecord;
import picocli.CommandLine;

import java.io.Console;
import java.security.GeneralSecurityException;

@CommandLine.Command(name="vault-view", version = "0.1", mixinStandardHelpOptions = true, requiredOptionMarker = '*')
public class VaultViewCommand implements VaultValidator {

    private static final Console console = System.console();

    @CommandLine.Option(names = {"-p", "--password"}, description = Labels.password)
    char[] password;

    @CommandLine.Option(names = {"-i", "--identifier"}, description = Labels.secretId)
    String id;

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
        if (id == null || id.isEmpty()) {
            id = ConsoleUtil.readRequiredParameter(console, Labels.secretId);
        }
        return id;
    }

    @Override
    public Integer call() throws Exception {
        if (isProfileValid()) {
            var profile = getProfileService().loadProfile(getPassword());
            if (profile.getProfileId() != null && !profile.getProfileId().isEmpty()) {
                var secretRecord = profile.getSecrets().get(getId());
                if (secretRecord != null) {
                    SecretInfo secret = getSecretInfo(profile, secretRecord);
                    printSecretRecord(secret);
                }
            }
            return CommandLine.ExitCode.OK;
        }
        return CommandLine.ExitCode.SOFTWARE;
    }

    private static SecretInfo getSecretInfo(PrivateProfile profile, SecretRecord secretRecord)
            throws GeneralSecurityException
    {
        var secret = new SecretInfo();
        secret.setRecordId(secretRecord.getRecordId());
        secret.setUsername(secretRecord.getUsername());
        secret.setPassword(secretRecord.getDecryptedPassword(profile.getPrivateKey()));
        secret.setGroupName(secretRecord.getGroupName());
        secret.setOrganizationCode(secretRecord.getOrganizationCode());
        secret.setEnvironmentName(secretRecord.getEnvironmentName());
        secret.setNote(secretRecord.getNote());
        return secret;
    }

    private void printSecretRecord(SecretInfo secretRecord) {
        System.out.println("\n");
        System.out.println(createGsonInstance().toJson(secretRecord));
        System.out.println("\n");
    }

    private Gson createGsonInstance() {
        return new GsonBuilder().setPrettyPrinting().create();
    }

}
