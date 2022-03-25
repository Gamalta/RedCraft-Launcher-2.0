package fr.gamalta.redcraft.launcher;

import fr.gamalta.redcraft.launcher.panel.Logging;
import fr.gamalta.redcraft.launcher.utils.Constants;
import fr.gamalta.redcraft.launcher.utils.Logger;
import fr.gamalta.redcraft.launcher.utils.Utils;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Launcher extends Application {

    private Utils utils = new Utils();

    public static void main(String[] args) {

        Logger.info("Lancement de l'application");
        Application.launch(args);

    }

    @Override
    public void start(Stage stage) {

        Logger.info("Preparation de la fenêtre...");

        stage.initStyle(StageStyle.DECORATED);
        stage.setResizable(false);
        stage.setTitle(Constants.TITLE);
        stage.setWidth(Constants.WIDTH);
        stage.setHeight(Constants.HEIGHT);
        stage.getIcons().add(utils.loadImage("logo.png"));
        stage.setOnCloseRequest(event -> {

            Platform.exit();
            System.exit(0);
        });

        Logger.info("Fenêtre configuré");
        Logger.info("Ajout des composants ...");
        stage.setScene(new Scene(new Logging().createContent()));
        Logger.info("Composants ajouté avec succès !");
        Logger.info("Essaie d'affichage du Panel de logging...");
        stage.show();
        Logger.info("Panel affiché avec succès !");
    }
}