package io.ns.sinduk.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * A utility to perform file based operations
 */
public class FileUtil {

    /**
     * Checks whether the file exists or not
     *
     * @param fqfn The fully qualified file name to check in the system
     * @return True if the file exists, false otherwise
     */
    public static boolean fileExists(String fqfn) {
        return new File(fqfn).exists();
    }

    /**
     * Removes the file from the sytem if exists
     *
     * @param fqfn The fully qualified file name to be deleted
     * @return True if the delete operation succeeds, false otherwise
     */
    public static boolean deleteFile(String fqfn) {
        if (fileExists(fqfn)) {
            return new File(fqfn).delete();
        }
        return false;
    }

    /**
     * Write the given content to a file
     *
     * @param fqfn The fully qualified file name to write the content
     * @param content The content to write in the file
     * @throws IOException If any error occurs during file writing operation
     */
    public static void writeToFile(String fqfn, String content) throws IOException {
        File file = new File(fqfn);
        file.getParentFile().mkdirs();
        file.createNewFile();
        try (FileOutputStream fos = new FileOutputStream(fqfn)) {
            fos.write(content.getBytes(StandardCharsets.UTF_8));
        }
    }

    /**
     * Read the content from a given file name
     *
     * @param fqfn The fully qualified file name to read the content from
     * @return The content of the given file name
     * @throws IOException If any error occurs during file read operation
     */
    public static String readFromFile(String fqfn) throws IOException {
        File file = new File(fqfn);
        try (FileInputStream fis = new FileInputStream(file)) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            byte[] fileBytes = bos.toByteArray();
            return new String(fileBytes, StandardCharsets.UTF_8);
        }
    }

}
