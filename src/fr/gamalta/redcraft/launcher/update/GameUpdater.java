package fr.gamalta.redcraft.launcher.update;

import fr.gamalta.redcraft.launcher.utils.LauncherFile;
import fr.gamalta.redcraft.launcher.utils.Logger;

import java.io.File;
import java.util.HashMap;

import static fr.gamalta.redcraft.launcher.utils.Constants.DIRECTORY;

public class GameUpdater {

    public static String CURRENT_FILE = "";
    static HashMap<String, LauncherFile> filesToUpdate = new HashMap<>();
    private GameDownloader downloadTask;
    private Thread updateThread;
    boolean updating = false;

    public void checkForUpdate() {

        File file = new File(DIRECTORY, "bin");
        file.getParentFile().mkdirs();

        Logger.info("Trying to create file: " + file.getPath());

        if (!file.exists()) {
            file.mkdirs();
        }

        updateThread = new Thread(() -> {

            GameParser.prepareFiles();
            downloadTask = new GameDownloader(GameUpdater.filesToUpdate, updateThread);
            updating = true;
            downloadTask.startDownloading();
        });

        updateThread.start();
    }
}
