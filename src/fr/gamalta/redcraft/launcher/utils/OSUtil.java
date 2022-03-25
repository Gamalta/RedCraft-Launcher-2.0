package fr.gamalta.redcraft.launcher.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;

import static fr.gamalta.redcraft.launcher.utils.Constants.RESSOURCE_LOCATION;

public class OSUtil
{
    public enum OperatingSystem {
        windows,
        macos,
        linux,
        unknown,;
    }

    public static OperatingSystem getOS() {
        String osName = System.getProperty("os.name").toLowerCase();

        if (osName.contains("win"))
            return OperatingSystem.windows;
        if (osName.contains("mac"))
            return OperatingSystem.macos;
        if (osName.contains("linux"))
            return OperatingSystem.linux;
        if (osName.contains("unix"))
            return OperatingSystem.linux;
        if (osName.contains("solaris"))
            return OperatingSystem.linux;
        if (osName.contains("sunos")) {
            return OperatingSystem.linux;
        }
        return OperatingSystem.unknown;
    }

    private static File getLocalStorage(String dir) {
        String userHome = System.getProperty("user.home");

        if (getOS() == OperatingSystem.windows)
            return new File(System.getenv("appdata"), "." + dir);
        if (getOS() == OperatingSystem.macos) {
            return new File(userHome, "Library/Application Support/" + dir);
        }
        return new File(userHome, dir);
    }


    public static File getDirectory() {

        return getLocalStorage(RESSOURCE_LOCATION);
    }

    public static File writeFile(String filename, String toWrite) {

        try {

            if (!getDirectory().exists()) {

                getDirectory().mkdir();
            }

            File file = new File(getDirectory(), filename);
            PrintStream out = new PrintStream(file.getAbsolutePath());
            out.print(toWrite);
            out.close();
            return file;

        } catch (FileNotFoundException e) {

            e.printStackTrace();

            return null;
        }
    }
    public static ArrayList<File> list(File folder) {

        ArrayList<File> files = new ArrayList<>();

        if (!folder.isDirectory()) {

            return files;
        }

        File[] folderFiles = folder.listFiles();

        if (folderFiles != null) {

            for (File f : folderFiles) {

                if (f.isDirectory()){
                    files.addAll(list(f));

                } else {
                    files.add(f);
                }
            }

        }  return files;
    }
}