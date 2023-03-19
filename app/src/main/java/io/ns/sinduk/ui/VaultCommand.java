package io.ns.sinduk.ui;


import io.ns.sinduk.ui.profile.ProfileCreateCommand;
import io.ns.sinduk.ui.profile.ProfileViewCommand;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(version = "0.1", mixinStandardHelpOptions = true,
        subcommands = {
                ProfileCreateCommand.class,
                ProfileViewCommand.class,
                VaultListCommand.class
        }
)
public class VaultCommand implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        return CommandLine.ExitCode.OK;
    }

}
