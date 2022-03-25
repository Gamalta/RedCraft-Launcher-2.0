package fr.gamalta.redcraft.launcher.utils;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.*;
import java.security.DigestInputStream;
import java.security.MessageDigest;


public class FileUtil {

    public static String getEtag(String etag) {

        if (etag == null) {

            etag = "-";

        } else if (etag.startsWith("\"") && etag.endsWith("\"")) {

            etag = etag.substring(1, etag.length() - 1);
        }

        return etag;
    }

    public static String getMD5(File file) {

        DigestInputStream stream = null;

        try {

            stream = new DigestInputStream(new FileInputStream(file), MessageDigest.getInstance("MD5"));
            byte[] buffer = new byte[65536];

            int read = stream.read(buffer);
            while (read >= 1)
                read = stream.read(buffer);

        } catch (Exception ignored) {

            return null;

        } finally {

            closeSilently(stream);
        }

        return String.format("%1$032x", new BigInteger(1, stream.getMessageDigest().digest()));
    }

    private static void closeSilently(Closeable closeable) {

        if (closeable != null) {

            try {

                closeable.close();

            } catch (IOException ignored) {

            }
        }
    }

    public static void deleteSomething(String path) {

        Path filePath_1 = Paths.get(path);

        try {

            Files.delete(filePath_1);

        } catch (NoSuchFileException x) {

            System.err.format("%s: no such file or directory%n", path);

        } catch (DirectoryNotEmptyException x) {

            System.err.format("%s not empty%n", path);

        } catch (IOException x) {

            x.printStackTrace();
        }
    }
}