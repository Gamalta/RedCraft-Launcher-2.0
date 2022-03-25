package fr.gamalta.redcraft.launcher.File;

import fr.gamalta.redcraft.launcher.utils.Logger;

import java.io.*;
import java.util.Properties;

import static fr.gamalta.redcraft.launcher.utils.Constants.*;


public class PropertiesSaver {

    public void loadUserProperties() {

        File file = new File(DIRECTORY + "/properties.redcraft");

        if (file.exists()) {
            Logger.info("Le fichier de configuration existe. Lecture !");
            loadProperties();

        } else {

            try {

                file.getParentFile().mkdirs();
                file.createNewFile();

            } catch (IOException e) {

                e.printStackTrace();
            }

            Logger.info("Fichier de configuration crée avec succès");
            saveDefaultProperties();
            loadProperties();
        }
    }

    private void saveDefaultProperties() {

        Properties properties = new Properties();
        FileOutputStream fileOutputStream = null;

        try {

            fileOutputStream = new FileOutputStream(DIRECTORY + "/properties.redcraft");
            properties.setProperty("accountName", "Pseudo");
            properties.setProperty("ramAllowed", "1G");
            properties.store(fileOutputStream, null);

        } catch (IOException io) {

            io.printStackTrace();

        } finally {

            if (fileOutputStream != null) {

                try {

                    fileOutputStream.close();

                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
        }
    }

    public void saveProperties() {

        Properties properties = new Properties();
        FileOutputStream fileOutputStream = null;

        try {

            fileOutputStream = new FileOutputStream(DIRECTORY + "/properties.redcraft");
            Logger.info("Identifiant sauvegardé: " + USERNAME);
            properties.setProperty("accountName", USERNAME);
            properties.setProperty("ramAllowed", RAM + "G");
            properties.store(fileOutputStream, null);

        } catch (IOException io) {

            io.printStackTrace();

        } finally {

            if (fileOutputStream != null) {

                try {

                    fileOutputStream.close();

                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
        }
    }


    private void loadProperties() {

        Properties properties = new Properties();
        FileInputStream fileInputStream = null;

        try {

            fileInputStream = new FileInputStream(DIRECTORY + "/properties.redcraft");
            properties.load(fileInputStream);
            Logger.info("Identifiant: " + properties.getProperty("accountName"));
            USERNAME = properties.getProperty("accountName");
            Logger.info("Ram: " + properties.getProperty("ramAllowed"));
            RAM = Integer.parseInt(properties.getProperty("ramAllowed").replace("G", ""));

        } catch (IOException io) {

            io.printStackTrace();

        } finally {

            if (fileInputStream != null) {

                try {

                    fileInputStream.close();

                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
        }
    }
}

