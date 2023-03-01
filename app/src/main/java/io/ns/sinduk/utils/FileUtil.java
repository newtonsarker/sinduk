package io.ns.sinduk.utils;

import java.io.File;
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

}
