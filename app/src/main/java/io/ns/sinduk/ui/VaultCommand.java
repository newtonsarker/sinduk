package io.ns.sinduk.ui;

import io.ns.sinduk.ui.profile.ProfileCreateCommand;
import io.ns.sinduk.ui.profile.ProfileViewCommand;
import io.ns.sinduk.ui.vault.VaultAddCommand;
import io.ns.sinduk.ui.vault.VaultListCommand;
import io.ns.sinduk.ui.vault.VaultViewCommand;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(version = "0.1", mixinStandardHelpOptions = true,
        subcommands = {
                ProfileCreateCommand.class,
                ProfileViewCommand.class,
                VaultAddCommand.class,
                VaultListCommand.class,
                VaultViewCommand.class
        }
)
public class VaultCommand implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        return CommandLine.ExitCode.OK;
    }

}
