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
        if (parameter == null) return console.readLine(label + ": ");
        return null;
    }

    public static String readParameterOrDefault(Console console, String label, String parameter, String defaultValue) {
        if (parameter != null && !parameter.isEmpty()) {
            return parameter;
        } else {
            String consoleValue = console.readLine(label + ": ");
            if (consoleValue != null && !consoleValue.isEmpty()) {
                return consoleValue;
            }
            return defaultValue;
        }
    }

    public static char[] readSecretOrDefault(Console console, String label, char[] parameter, char[] defaultValue) {
        if (parameter != null && parameter.length > 0) {
            return parameter;
        } else {
            char[] consoleValue = console.readPassword(label + ": ");
            if (consoleValue == null || consoleValue.length < 1) {
                return defaultValue;
            }
            return consoleValue;
        }
    }

}
