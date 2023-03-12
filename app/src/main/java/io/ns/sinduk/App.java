package io.ns.sinduk;


import io.ns.sinduk.services.ProfileService;
import io.ns.sinduk.services.ProfileServiceImpl;
import io.ns.sinduk.ui.VaultCommand;
import picocli.CommandLine;

public class App {

    public void start(String[] args) {
        AppContext.registerObject(ProfileService.class, new ProfileServiceImpl());

        new CommandLine(new VaultCommand()).execute(args);
    }

    public static void main(String[] args) {
        new App().start(args);
    }

}
