package io.ns.sinduk.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FileUtil {

    public static boolean fileExists(String fqfn) {
        return new File(fqfn).exists();
    }

    public static boolean deleteFile(String fqfn) {
        if (fileExists(fqfn)) {
            return new File(fqfn).delete();
        }
        return false;
    }

    public static void writeToFile(String fqfn, String content) throws IOException {
        File file = new File(fqfn);
        file.getParentFile().mkdirs();
        file.createNewFile();
        try (FileOutputStream fos = new FileOutputStream(fqfn)) {
            fos.write(content.getBytes(StandardCharsets.UTF_8));
        }
    }

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
