package fr.gamalta.redcraft.launcher.panel;

import fr.gamalta.redcraft.launcher.Authentication;
import fr.gamalta.redcraft.launcher.File.PropertiesSaver;
import fr.gamalta.redcraft.launcher.update.GameUpdater;
import fr.gamalta.redcraft.launcher.utils.Logger;
import fr.gamalta.redcraft.launcher.utils.Utils;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.Objects;

import static fr.gamalta.redcraft.launcher.utils.Constants.*;

public class Logging {

	private Utils utils;
	private PropertiesSaver propertiesSaver;
	private GameUpdater gameUpdater = new GameUpdater();
	private TextField usernameField = new TextField();
	private TextField passwordTextField = new TextField();
	private Button loginButton = new Button();
	private Button settingsButton = new Button();
	private Rectangle rectangle = new Rectangle();
	private Rectangle downloadRectangle = new Rectangle();
	private Label usernameTitle = new Label();
	private Label passwordTitle = new Label();
	private Label downloadLabel = new Label();
	public static Label downloadPercentLabel = new Label();
	public static Label downloadFileLabel = new Label();
	private CheckBox viewPassword = new CheckBox();
	private PasswordField passwordField = new PasswordField();
	public static ProgressBar progressBar = new ProgressBar();
	public static Timeline timeline;
	public static Timeline timeline_;

	public Logging() {

		utils = new Utils();
		propertiesSaver = new PropertiesSaver();
		utils.lodFont("Roboto-Light.ttf");
		propertiesSaver.loadUserProperties();
	}

	public Parent createContent() {

		Pane contentPane = new Pane();
		String roboto_Light = "Roboto Lt";
		contentPane.setPrefSize(WIDTH, HEIGHT);

		MediaPlayer player = new MediaPlayer(utils.getMedia("background.mp4"));
		MediaView viewer = new MediaView(player);
		viewer.setFitWidth(WIDTH);
		viewer.setFitHeight(HEIGHT);
		player.setAutoPlay(true);
		player.setMute(true);
		viewer.setPreserveRatio(false);
		player.setCycleCount(-1);
		player.play();

		rectangle.setX(WIDTH / 3);
		rectangle.setY(0);
		rectangle.setWidth(WIDTH / 3);
		rectangle.setHeight(HEIGHT / 6 * 4);
		rectangle.setOpacity(0.5);

		downloadRectangle.setHeight(HEIGHT);
		downloadRectangle.setWidth(WIDTH);
		downloadRectangle.setX(0);
		downloadRectangle.setY(0);
		downloadRectangle.setFill(Color.BLACK);
		downloadRectangle.setOpacity(0.0D);
		downloadRectangle.setDisable(true);
		downloadRectangle.setVisible(false);

		ImageView logoImage = new ImageView();
		logoImage.setImage(utils.loadImage("logo.png"));
		logoImage.setFitWidth(128);
		logoImage.setFitHeight(128);
		logoImage.setLayoutX(WIDTH / 2 - 48);
		logoImage.setLayoutY(70);
		AnimationTimer animate = new AnimationTimer() {
			@Override
			public void handle(long now) {
				float multplicatorSize = 54.25F;
				int mouseX = MouseInfo.getPointerInfo().getLocation().x;
				int mouseY = MouseInfo.getPointerInfo().getLocation().y;
				logoImage.setLayoutX(WIDTH / 2 - 48 - mouseX / multplicatorSize);
				logoImage.setLayoutY(70 - mouseY / multplicatorSize);
			}
		};
		animate.start();

		usernameTitle.setText("Identifiant:");
		usernameTitle.setFont(Font.font(roboto_Light, 18));
		usernameTitle.setTextFill(Color.WHITE);
		usernameTitle.setLayoutX(WIDTH / 2 - 125);
		usernameTitle.setLayoutY(240);

		usernameField.setPromptText("Identifiant");
		usernameField.setLayoutX(WIDTH / 2 - 125);
		usernameField.setLayoutY(270);
		usernameField.setPrefSize(250, 30);
		usernameField.setStyle("-fx-background-color: rgba(53, 89, 119, 0.4); -fx-text-fill: white;");
		usernameField.setText(USERNAME.equals("Pseudo") ? "" : USERNAME);
		usernameField.setFont(Font.font(roboto_Light, 18));
		usernameField.setOnKeyPressed(event -> {

			if (event.getCode() == KeyCode.ENTER) {

				launch();
			}
		});

		usernameField.deselect();
		passwordTitle.setText("Mot de passe:");
		passwordTitle.setFont(Font.font(roboto_Light, 18));
		passwordTitle.setTextFill(Color.WHITE);
		passwordTitle.setLayoutX(WIDTH / 2 - 125);
		passwordTitle.setLayoutY(320);

		passwordField.setPromptText("Mot de passe");
		passwordField.setPrefSize(250, 30);
		passwordField.setLayoutX(WIDTH / 2 - 125);
		passwordField.setLayoutY(350);
		passwordField.setStyle("-fx-background-color: rgba(53, 89, 119, 0.4); -fx-text-fill: white;");
		passwordField.setFont(Font.font(roboto_Light, 18));
		passwordField.managedProperty().bind(viewPassword.selectedProperty().not());
		passwordField.visibleProperty().bind(viewPassword.selectedProperty().not());
		passwordField.setOnKeyPressed(event -> {

			if (event.getCode() == KeyCode.ENTER) {

				launch();
			}
		});

		passwordTextField.setPrefSize(250, 30);
		passwordTextField.setLayoutX(WIDTH / 2 - 125);
		passwordTextField.setLayoutY(350);
		passwordTextField.setPromptText("Mot de passe");
		passwordTextField.setStyle("-fx-background-color: rgba(53, 89, 119, 0.4); -fx-text-fill: white;");
		passwordTextField.setFont(Font.font(roboto_Light, 18));
		passwordTextField.setManaged(false);
		passwordTextField.setVisible(false);
		passwordTextField.managedProperty().bind(viewPassword.selectedProperty());
		passwordTextField.visibleProperty().bind(viewPassword.selectedProperty());
		passwordTextField.textProperty().bindBidirectional(passwordField.textProperty());
		passwordTextField.setOnKeyPressed(event -> {

			if (event.getCode() == KeyCode.ENTER) {

				launch();
			}
		});

		downloadPercentLabel.setText("MISE A JOUR");
		downloadPercentLabel.setTextFill(Color.WHITE);
		downloadPercentLabel.setFont(Font.font(roboto_Light, 80));
		downloadPercentLabel.setPrefSize(WIDTH, 50.0D);
		downloadPercentLabel.setLayoutX(0.0D);
		downloadPercentLabel.setLayoutY(150.0D);
		downloadPercentLabel.setTextAlignment(TextAlignment.CENTER);
		downloadPercentLabel.setAlignment(Pos.CENTER);
		downloadPercentLabel.setDisable(true);
		downloadPercentLabel.setVisible(false);

		downloadFileLabel.setText("Fichier inconnu");
		downloadFileLabel.setTextFill(Color.WHITE);
		downloadFileLabel.setFont(Font.font(roboto_Light, 80));
		downloadFileLabel.setPrefSize(WIDTH, 50.0D);
		downloadFileLabel.setTextAlignment(TextAlignment.CENTER);
		downloadFileLabel.setAlignment(Pos.CENTER);
		downloadFileLabel.setLayoutX(0.0D);
		downloadFileLabel.setLayoutY(250.0D);
		downloadFileLabel.setDisable(true);
		downloadFileLabel.setVisible(false);

		viewPassword.setLayoutX(WIDTH / 2 + 95);
		viewPassword.setLayoutY(350);
		viewPassword.setPrefSize(35, 35);

		downloadLabel.setFont(Font.font(roboto_Light, 18));
		downloadLabel.setTextFill(Color.WHITE);
		downloadLabel.setLayoutX(WIDTH / 2 - 15);
		downloadLabel.setLayoutY(HEIGHT - 67);

		loginButton.setStyle("-fx-background-color: rgba(53, 89, 119, 0.4); -fx-text-fill: white;");
		loginButton.setFont(Font.font(roboto_Light, 18));
		loginButton.setText("Se connecter");
		loginButton.setLayoutX(WIDTH / 2 - 125);
		loginButton.setLayoutY(400.0);
		loginButton.setPrefSize(250.0, 30.0);
		loginButton.onMouseClickedProperty().set(event -> launch());

		BackgroundSize backgroundSize = new BackgroundSize(35.0, 35.0, false, false, false, false);
		BackgroundImage backgroundImage = new BackgroundImage(utils.loadImage("options.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, backgroundSize);
		Background background = new Background(backgroundImage);

		settingsButton.setBackground(background);
		settingsButton.setLayoutX(WIDTH - 45);
		settingsButton.setLayoutY(5.0);
		settingsButton.setPrefSize(35.0, 35.0);
		settingsButton.setOnMouseEntered(event -> settingsButton.setCursor(Cursor.HAND));
		settingsButton.onMouseClickedProperty().set(event -> Settings.createPanel());
		contentPane.getChildren().addAll(viewer, rectangle, logoImage, usernameTitle, usernameField, passwordTitle, passwordTextField, passwordField, viewPassword, loginButton, settingsButton, downloadRectangle, downloadFileLabel, downloadPercentLabel);

		return contentPane;
	}

	private void launch() {

		if (RAM == 1) {

			if(!Objects.requireNonNull(Logger.alert(Logger.AlertType.RAM)).equals(ButtonType.OK)){

				Settings.createPanel();
				return;
			}

		}

		if (!usernameField.getText().isEmpty() && !passwordField.getText().isEmpty()) {

			USERNAME = usernameField.getText();
			propertiesSaver.saveProperties();

			Logger.info("Premium connection");
			new Authentication(usernameField.getText(), passwordField.getText(), true);

			if (!UUID.equals("0")) {

				usernameField.setDisable(true);
				passwordField.setDisable(true);
				loginButton.setDisable(true);

				updateAndLaunch();

			} else {

				Logger.alert(Logger.AlertType.LOGGING);
			}
		} else if (!usernameField.getText().isEmpty() && passwordField.getText().isEmpty()) {

			if (Objects.requireNonNull(Logger.alert(Logger.AlertType.CRACK)).equals(ButtonType.OK)) {

				Logger.info("Crack connection");
				new Authentication(usernameField.getText(), passwordField.getText(), false);

				if (!UUID.equals("0")) {

					usernameField.setDisable(true);
					loginButton.setDisable(true);

					propertiesSaver.saveProperties();
					updateAndLaunch();
				}
			}
		} else {

			Logger.alert(Logger.AlertType.LOGGING);
		}
	}

	private void updateAndLaunch() {

		downloadRectangle.setDisable(false);
		downloadRectangle.setVisible(true);
		downloadRectangle.setOpacity(0.8D);
		downloadFileLabel.setDisable(false);
		downloadFileLabel.setVisible(true);
		downloadPercentLabel.setDisable(false);
		downloadPercentLabel.setVisible(true);

		Logger.info("Vérification des fichiers...");

		timeline = new Timeline(new KeyFrame(Duration.seconds(0.0D), e -> downloadPercentLabel.setText(new DecimalFormat(".##").format(progressBar.getProgress() * 100.0D) + "%")), new KeyFrame(Duration.seconds(0.1D)));
		timeline_ = new Timeline(new KeyFrame(Duration.seconds(0.0D), e -> downloadFileLabel.setText(GameUpdater.CURRENT_FILE)), new KeyFrame(Duration.seconds(0.1D)));
		timeline.setCycleCount(-1);
		timeline_.setCycleCount(-1);
		timeline.play();
		timeline_.play();

		gameUpdater.checkForUpdate();
	}
}
