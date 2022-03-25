package fr.gamalta.redcraft.launcher.update;

import fr.gamalta.redcraft.launcher.panel.Logging;
import fr.gamalta.redcraft.launcher.utils.FileUtil;
import fr.gamalta.redcraft.launcher.utils.LauncherFile;
import fr.gamalta.redcraft.launcher.utils.Logger;
import javafx.application.Platform;

import java.io.File;
import java.util.HashMap;

import static fr.gamalta.redcraft.launcher.utils.Constants.DIRECTORY;
import static fr.gamalta.redcraft.launcher.utils.Constants.DOWNLOAD_URL;

class GameDownloader {

    static int downloadedFiles;
    static int needToDownload;
    private static HashMap<String, LauncherFile> customFiles = new HashMap<>();
    private static String totalFiles;
    private Thread updateThread;
    private GameUpdater updater;

    GameDownloader(HashMap<String, LauncherFile> totalFiles, Thread thread) {

        updater = new GameUpdater();
        customFiles = totalFiles;
        updateThread = thread;
        Logger.info(needToDownload + " fichiers ont besoin d'être téléchargé.");
    }

    static void addToVerifyIntegrity(String addTo) {

        totalFiles = totalFiles + " " + addTo;
    }

    void startDownloading() {

        for (String name : customFiles.keySet()) {

            String fileName = name.replaceAll(DOWNLOAD_URL, "");
            int index = fileName.lastIndexOf("\\");
            String dirLocation = fileName.substring(index + 1);

            if (!name.endsWith("/")) {

                FileDownloader.download(name, DIRECTORY + File.separator + dirLocation, dirLocation);
            }
        }
        updateThread.interrupt();
        updater.updating = false;
        Logger.info("Téléchargement finit.");
        Logging.timeline.stop();
        Logging.timeline_.stop();
        Logger.info("Vérification de l'installation...");
        Platform.runLater(() -> Logging.downloadPercentLabel.setText("Verification"));
        Platform.runLater(() -> Logging.downloadFileLabel.setText("Verification des fichiers"));
        verifyIntegrity(DIRECTORY);
        Platform.runLater(() -> Logging.downloadPercentLabel.setText("Lancement..."));
        Platform.runLater(() -> Logging.downloadFileLabel.setText("Lancement du jeu"));

        GameLaunch.createInstance();
    }

    private void verifyIntegrity(File dir) {

        File[] subDirs = dir.listFiles();
        int folderIndex;

        if (dir.isDirectory()) {

            assert subDirs != null;
            for (folderIndex = 0; folderIndex < subDirs.length; folderIndex++) {

                File nextFolder = subDirs[folderIndex];
                verifyIntegrity(nextFolder);
                File location = subDirs[folderIndex];

                if (location.isFile()) {

                    File f = new File(DIRECTORY + "/" + subDirs[folderIndex].toString().replace(DIRECTORY + "\\", ""));
                    boolean exists = exists(subDirs[folderIndex].toString().replace(DIRECTORY + "\\", ""));

                    if (!exists && !f.getName().contains("properties.redcraft")) {

                        String parentDirName = f.getAbsoluteFile().getParent();
                        if (!parentDirName.contains("bin") && !parentDirName.contains("logs") && !parentDirName.contains("natives")) {

                            FileUtil.deleteSomething(f.getAbsolutePath());
                        }
                    }
                }
            }
        }
    }

    private boolean exists(String fll) {

        return totalFiles != null && totalFiles.contains(fll.replace("\\", "/"));
    }
}