package fr.gamalta.redcraft.launcher.utils;

import java.io.File;

public class Constants {

	public static String TITLE = "RedCraft launcher";
	public static int WIDTH = 1280;
	public static int HEIGHT = 720;
	static String RESSOURCE_LOCATION = "RedCraft/Minecraft";
	public static String USERNAME = "Email";
	public static String PSEUDO = "Pseudo";
	public static String PASSWORD = "";
	public static String UUID = "0";
	public static String ACCESS_TOKEN = "0";
	public static String CLIENT_TOKEN = "0";
	public static String USER_TYPE = "legacy";
	public static int RAM = 1;
	public static String DOWNLOAD_URL = "https://save.redblock.fr/app/webroot/RedCraft/launcher/";
	public static File DIRECTORY = new File(System.getenv("appdata"), "." + RESSOURCE_LOCATION);
}
