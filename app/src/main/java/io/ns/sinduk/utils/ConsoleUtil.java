package io.ns.sinduk.utils;

import java.io.Console;

public class ConsoleUtil {

    public static String readRequiredParameter(Console console, String label) {
        var parameter = console.readLine(label + ": ");
        if (parameter == null || parameter.trim().isEmpty()) {
            return readRequiredParameter(console, label);
        }
        return parameter;
    }

    public static char[] readRequiredSecret(Console console, String label) {
        var parameter = console.readPassword(label + ": ");
        if (parameter == null || parameter.length < 1) {
            return readRequiredSecret(console, label);
        }
        return parameter;
    }

    public static String readOptionalParameter(Console console, String label, String parameter) {
        if (parameter == null) {
            return console.readLine(label + ": ");
        }
        return null;
    }

}
