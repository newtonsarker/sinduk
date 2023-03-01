package io.ns.sinduk.utils;

import java.util.Base64;

public class Base64Util {

    private static Base64.Encoder encoder = Base64.getEncoder();

    public static String encodeToString(byte[] payload) {
        return encoder.encodeToString(payload);
    }

}
