package fr.gamalta.redcraft.launcher.utils;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Optional;

public class Logger {

    public static String getLoggerPrefix(){

        return "[" + new SimpleDateFormat("dd/MM HH:mm:ss").format(Calendar.getInstance().getTime()) + "] ";
    }
    public static void info(String message){

        System.out.println(getLoggerPrefix() + message);

    }

    public static void warning(String message){

        System.err.println(getLoggerPrefix() + message);
        System.err.println(getLoggerPrefix() + "Des problèmes potentiels ont été détectés.");
        Alert warningAlert = new Alert(Alert.AlertType.ERROR);
        warningAlert.setTitle("RedCraft Launcher Avertissement");
        warningAlert.setHeaderText("Des problèmes sont parvenus.");
        warningAlert.setContentText(message);
        warningAlert.showAndWait();

    }

    public static void error(String message){

        System.err.println(getLoggerPrefix() + message);
        System.err.println(getLoggerPrefix() + "Une erreur fatale s'est produite, le lanceur c'est arrêtée de force.");
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("RedCraft Launcher Erreur");
        errorAlert.setHeaderText("Une erreur est survenue !");
        errorAlert.setContentText(message);
        errorAlert.showAndWait();
        Platform.exit();
        System.exit(1);
    }

    public static ButtonType alert(AlertType alertType){

        switch (alertType) {
            case LOGGING:

                System.err.println(getLoggerPrefix() + "Une erreur s'est produite, lors de la connexion les identifaints sont incorectes");
                Alert loggingAlert = new Alert(Alert.AlertType.INFORMATION);
                loggingAlert.setTitle("RedCraft Launcher Erreur");
                loggingAlert.setHeaderText("Une erreur est survenue, authentification échouée !");
                loggingAlert.setContentText("Identifiant ou mot de passe incorrecte");
                loggingAlert.show();
                return null;

            case RAM:

                System.err.println(getLoggerPrefix() + "Un avertissement est apparu, le client essaye de lancer sont jeu avec 1 seul giga de ram.");
                Alert ramAlert = new Alert(Alert.AlertType.CONFIRMATION);
                ramAlert.setTitle("RedCraft Launcher Alerte");
                ramAlert.setHeaderText("Attention !");
                ramAlert.setContentText("Voulez vous vraiment lancer Minecraft avec un 1Gb de ram ?");
                Optional<ButtonType> ramResult = ramAlert.showAndWait();
                return ramResult.get();

            case CRACK:;

                System.err.println(getLoggerPrefix() + "Un avertissement est apparu, le client n'a pas entré de mots de passe");
                Alert crackAlert = new Alert(Alert.AlertType.CONFIRMATION);
                crackAlert.setTitle("RedCraft Launche Alerte");
                crackAlert.setHeaderText("Attention !");
                crackAlert.setContentText("Vous n'avez pas entrer de mots de passe voulez lancer Minecraft en mode offline 'en cracké' ?");
                Optional<ButtonType> crackResult = crackAlert.showAndWait();
                return crackResult.get();

            default:
                throw new IllegalStateException("Unexpected value: " + alertType);
        }
    }
    public enum AlertType {

        LOGGING,
        RAM,
        CRACK;
    }
}
