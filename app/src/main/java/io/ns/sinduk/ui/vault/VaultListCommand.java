package io.ns.sinduk.ui.vault;

import io.ns.sinduk.AppContext;
import io.ns.sinduk.services.ProfileService;
import io.ns.sinduk.ui.Labels;
import io.ns.sinduk.ui.VaultValidator;
import io.ns.sinduk.utils.ConsoleUtil;
import io.ns.sinduk.vo.PrivateProfile;
import io.ns.sinduk.vo.SecretRecord;
import picocli.CommandLine;
import picocli.CommandLine.Help.Column;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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

    @CommandLine.Option(names = {"-f", "--find"}, description = Labels.find)
    String findTerm;

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

    private ArrayList<SecretRecord> copySecretList(PrivateProfile profile) {
        var secrets = new ArrayList<SecretRecord>();
        profile.getSecrets().values().stream().filter(this::findByTerm).forEach(secrets::add);
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
        return new String[] {
                secretRecord.getRecordId(),
                secretRecord.getUsername(),
                secretRecord.getGroupName(),
                secretRecord.getOrganizationCode(),
                secretRecord.getEnvironmentName()
        };
    }

    private Column[] createColumnConfig(ArrayList<SecretRecord> secrets) {
        final var colDividerWidth = 3;
        return new Column[] {
                // record id
                new Column(maxLength(secrets, 0) + colDividerWidth, 2, Column.Overflow.SPAN),

                // user name
                new Column(maxLength(secrets, 1) + colDividerWidth, 2, Column.Overflow.SPAN),

                // group name
                new Column(maxLength(secrets, 2) + colDividerWidth, 2, Column.Overflow.SPAN),

                // organization code
                new Column(maxLength(secrets, 3) + colDividerWidth, 2, Column.Overflow.SPAN),

                // environment
                new Column(maxLength(secrets, 4) + colDividerWidth, 2, Column.Overflow.SPAN)
        };
    }

    private int maxLength(List<SecretRecord> secretRecords, int colIndex) {
        final var length = new AtomicInteger();
        secretRecords.stream()
                .filter(secretRecord -> secretRecord.getRecordId() != null)
                .forEach(secretRecord -> {
                    switch (colIndex) {
                        case 0 -> getColMaxLength(length, COLUMN_ID, secretRecord.getRecordId());
                        case 1 -> getColMaxLength(length, COLUMN_USER_NAME, secretRecord.getUsername());
                        case 2 -> getColMaxLength(length, COLUMN_GROUP_NAME, secretRecord.getGroupName());
                        case 3 -> getColMaxLength(length, COLUMN_ORGANIZATION_CODE, secretRecord.getOrganizationCode());
                        case 4 -> getColMaxLength(length, COLUMN_ENVIRONMENT, secretRecord.getEnvironmentName());
                    }
                });
        return length.get();
    }

    private static void getColMaxLength(AtomicInteger length, String headerName, String columnValue) {
        length.set(Math.max(Math.max(length.get(), headerName.length()), columnValue.length()));
    }

    private boolean findByTerm(SecretRecord secretRecord) {
        return findTerm == null
                || findTerm.trim().isEmpty()
                || containsFindTerm(secretRecord.getUsername())
                || containsFindTerm(secretRecord.getGroupName())
                || containsFindTerm(secretRecord.getOrganizationCode())
                || containsFindTerm(secretRecord.getEnvironmentName())
                || containsFindTerm(secretRecord.getNote());
    }

    private boolean containsFindTerm(String value) {
        return (value != null)
                && !value.isEmpty()
                && value.toLowerCase(Locale.ROOT).contains(findTerm.trim().toLowerCase());
    }

}
