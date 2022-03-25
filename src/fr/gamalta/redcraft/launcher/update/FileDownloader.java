package fr.gamalta.redcraft.launcher.update;

import fr.gamalta.redcraft.launcher.panel.Logging;
import fr.gamalta.redcraft.launcher.utils.Logger;
import javafx.application.Platform;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

class FileDownloader {

    private static double percentage = 0.0D;

    static void download(String fileUrl, String fileName, String flNm) {

        Logger.info("Téléchargement: " + flNm);
        String[] parts = flNm.split("/");
        GameUpdater.CURRENT_FILE = parts[parts.length - 1];

        try {

            URL url = new URL(fileUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            File locate = new File(fileName);

            if (!locate.exists()) {

                locate.getParentFile().mkdirs();
                locate.createNewFile();
            }

            BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
            FileOutputStream fos = new FileOutputStream(fileName);
            BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
            byte[] data = new byte[1024];
            int read;
            while ((read = in.read(data, 0, 1024)) >= 0) {
                bout.write(data, 0, read);
                percentage = GameDownloader.downloadedFiles * 1.0D / GameDownloader.needToDownload;
            }
            Platform.runLater(() -> Logging.progressBar.setProgress(percentage));
            bout.close();
            in.close();
            GameDownloader.downloadedFiles++;

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}