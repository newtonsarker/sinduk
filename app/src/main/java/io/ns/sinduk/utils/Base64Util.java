package io.ns.sinduk.utils;

import java.util.Base64;

/**
 * A utility to encode or decode given payload based on base64 scheme
 */
public class Base64Util {

    private static Base64.Encoder encoder = Base64.getEncoder();
    private static Base64.Decoder decoder = Base64.getDecoder();

    /**
     * Encode the given byte array
     *
     * @param payload The byte array to be encoded as string
     * @return The base64 encoded string
     */
    public static String encodeToString(byte[] payload) {
        return encoder.encodeToString(payload);
    }

    /**
     * Decode the base64 encoded string
     *
     * @param payload The string to be decoded
     * @return The decoded byte array
     */
    public static byte[] decodeToBytes(String payload) {
        return decoder.decode(payload);
    }

}
