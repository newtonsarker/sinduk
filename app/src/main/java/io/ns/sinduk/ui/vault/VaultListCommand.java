package io.ns.sinduk.ui.vault;

import io.ns.sinduk.AppContext;
import io.ns.sinduk.services.ProfileService;
import io.ns.sinduk.ui.Labels;
import io.ns.sinduk.ui.VaultValidator;
import io.ns.sinduk.utils.ConsoleUtil;
import io.ns.sinduk.vo.PrivateProfile;
import io.ns.sinduk.vo.SecretRecord;
import picocli.CommandLine;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


@CommandLine.Command(name="vault-list", version = "0.1", mixinStandardHelpOptions = true, requiredOptionMarker = '*')
public class VaultListCommand implements VaultValidator {

    private static final Console console = System.console();

    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_USER_NAME = "USER NAME";
    private static final String COLUMN_GROUP_NAME = "GROUP NAME";
    private static final String COLUMN_ORGANIZATION_CODE = "ORGANIZATION";
    private static final String COLUMN_ENVIRONMENT = "ENVIRONMENT";

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
                var table = createTable(copySecretList(profile));
                printTable(table);
            }
            return CommandLine.ExitCode.OK;
        }
        return CommandLine.ExitCode.SOFTWARE;
    }

    private static void printTable(CommandLine.Help.TextTable table) {
        System.out.println("\n");
        System.out.println(CommandLine.Help.Ansi.AUTO.string(table.toString()));
        System.out.println("\n");
    }

    private static ArrayList<SecretRecord> copySecretList(PrivateProfile profile) {
        var secrets = new ArrayList<SecretRecord>();
        profile.getSecrets().forEach((id, record) -> secrets.add(record));
        return secrets;
    }

    private CommandLine.Help.TextTable createTable(ArrayList<SecretRecord> secrets) {
        // configure table
        var table = CommandLine.Help.TextTable.forColumns(
                CommandLine.Help.defaultColorScheme(CommandLine.Help.Ansi.AUTO),
                createColumnConfig(secrets)
        );

        // add table header
        table.addRowValues(
                COLUMN_ID,
                COLUMN_USER_NAME,
                COLUMN_GROUP_NAME,
                COLUMN_ORGANIZATION_CODE,
                COLUMN_ENVIRONMENT
        );

        // add table rows
        secrets.forEach(secretRecord -> table.addRowValues(createRow(secretRecord)));

        return table;
    }

    private String[] createRow(SecretRecord secretRecord) {
        return new String[]{
                secretRecord.getRecordId(),
                secretRecord.getUsername(),
                secretRecord.getGroupName(),
                secretRecord.getOrganizationCode(),
                secretRecord.getEnvironmentName()
        };
    }

    private CommandLine.Help.Column[] createColumnConfig(ArrayList<SecretRecord> secrets) {
        return new CommandLine.Help.Column[] {
                // record id
                new CommandLine.Help.Column(maxLength(secrets, 0), 2, CommandLine.Help.Column.Overflow.SPAN),

                // user name
                new CommandLine.Help.Column(maxLength(secrets, 1), 2, CommandLine.Help.Column.Overflow.SPAN),

                // group name
                new CommandLine.Help.Column(maxLength(secrets, 2), 2, CommandLine.Help.Column.Overflow.SPAN),

                // organization code
                new CommandLine.Help.Column(maxLength(secrets, 3), 2, CommandLine.Help.Column.Overflow.SPAN),

                // environment
                new CommandLine.Help.Column(maxLength(secrets, 4), 2, CommandLine.Help.Column.Overflow.SPAN)
        };
    }

    private int maxLength(List<SecretRecord> secretRecords, int colIndex) {
        AtomicInteger result = new AtomicInteger();
        secretRecords.stream()
                .filter(secretRecord -> secretRecord.getRecordId() != null)
                .forEach(secretRecord -> {
                    switch (colIndex) {
                        case 0 -> result.set(Math.max(Math.max(result.get(), COLUMN_ID.length()), secretRecord.getRecordId().length()));
                        case 1 -> result.set(Math.max(Math.max(result.get(), COLUMN_USER_NAME.length()), secretRecord.getUsername().length()));
                        case 2 -> result.set(Math.max(Math.max(result.get(), COLUMN_GROUP_NAME.length()), secretRecord.getGroupName().length()));
                        case 3 -> result.set(Math.max(Math.max(result.get(), COLUMN_ORGANIZATION_CODE.length()), secretRecord.getOrganizationCode().length()));
                        case 4 -> result.set(Math.max(Math.max(result.get(), COLUMN_ENVIRONMENT.length()), secretRecord.getEnvironmentName().length()));
                    }
                });

        var defaultLength = 3;
        return result.get() + defaultLength;
    }

}
