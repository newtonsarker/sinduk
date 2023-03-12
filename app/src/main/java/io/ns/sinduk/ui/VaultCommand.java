package io.ns.sinduk.ui;


import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(name="vault", version = "0.1", mixinStandardHelpOptions = true,
        subcommands = {
                VaultCreateCommand.class,
                VaultListCommand.class
        }
)
public class VaultCommand implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        return CommandLine.ExitCode.OK;
    }

}
