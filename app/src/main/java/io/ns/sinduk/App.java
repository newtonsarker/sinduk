/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package io.ns.sinduk;

import io.ns.sinduk.services.ProfileServiceImpl;

public class App {
    public String start() {
        var profileService = new ProfileServiceImpl();

        profileService.profileExists();

        return "Hello World!";
    }

    public static void main(String[] args) {
        System.out.println(new App().start());
    }
}
