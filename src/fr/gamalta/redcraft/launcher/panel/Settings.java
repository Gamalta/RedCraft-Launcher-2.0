package fr.gamalta.redcraft.launcher.panel;

import fr.gamalta.redcraft.launcher.utils.Logger;
import javafx.scene.control.ChoiceDialog;

import java.util.ArrayList;
import java.util.Optional;

import static fr.gamalta.redcraft.launcher.utils.Constants.RAM;

public class Settings {

    public static void createPanel() {

        ArrayList<String> choices = new ArrayList<>();

        for (int i = 1; i <= 16; i++) {

            choices.add(i + "G");
        }
        ChoiceDialog<String> dialog = new ChoiceDialog<>(RAM + "G", choices);
        dialog.setTitle("Paramètre:");
        dialog.setHeaderText("Paramètres de Lancement");
        dialog.setContentText("RAM allouée: ");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(s -> {
            RAM = Integer.parseInt(s.replace("G", ""));
            Logger.info("Ram sauvegardé: " + RAM);
        });
    }
}
