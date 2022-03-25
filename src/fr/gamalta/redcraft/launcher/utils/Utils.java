package fr.gamalta.redcraft.launcher.utils;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.text.Font;

import javax.imageio.ImageIO;

public class Utils {


    public Media getMedia(String media) {

        try {

            return new Media(this.getClass().getResource("/resources/" + media).toURI().toString());

        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

    public Image loadImage(String image) {

        try {

            return SwingFXUtils.toFXImage(ImageIO.read(this.getClass().getResource("/resources/" + image)), null);

        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

    public void lodFont(String fontFile) {


        Font.loadFont(this.getClass().getResourceAsStream(Constants.RESSOURCE_LOCATION + fontFile), 14);
    }
}
