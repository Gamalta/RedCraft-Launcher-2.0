package fr.gamalta.redcraft.launcher.utils;

public class LauncherFile {

    private double size;
    private String url;
    private String path;

    public LauncherFile(double size, String url, String path) {
        this.size = size;
        this.url = url;
        this.path = path;
    }

    public String toString() {

        return "<" + size + ", " + url + ", " + path + ">";
    }
}