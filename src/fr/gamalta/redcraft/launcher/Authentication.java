package fr.gamalta.redcraft.launcher;

import fr.gamalta.redcraft.launcher.utils.Logger;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static fr.gamalta.redcraft.launcher.utils.Constants.*;

public class Authentication {

	public Authentication(String username, String password, Boolean premium) {

		if (premium) {

			USERNAME = username;
			PASSWORD = password;
			USER_TYPE = "premium";
			tryLogin();

		} else {

			Random rand = new Random();
			ACCESS_TOKEN = "account_offline-" + rand.nextInt(10000);
			CLIENT_TOKEN = "account_offline-" + rand.nextInt(10000);
			UUID = "account_offline-" + rand.nextInt(10000);
			USER_TYPE = "legacy";
			PSEUDO = USERNAME;
		}
	}

	private void tryLogin() {

		Logger.info("Connection en cours...");
		String payload = "{\"agent\":{\"name\":\"Minecraft\",\"version\":1},\"username\":\"" + USERNAME + "\",\"password\":\"" + PASSWORD + "\"}";
		String requestUrl = "https://authserver.mojang.com/authenticate";

		String jsonString = sendPostRequest(requestUrl, payload);

		Logger.info(jsonString);

		if(jsonString != null) {

			JSONObject jsonObject = new JSONObject(jsonString);
			JSONObject jsonObject1 = jsonObject.getJSONObject("selectedProfile");
			PSEUDO = jsonObject1.getString("name");
			Logger.info("Pseudo: " + PSEUDO);
			UUID = jsonObject1.getString("id");
			Logger.info("uuid: " + UUID);
			ACCESS_TOKEN = jsonObject.getString("accessToken");
			Logger.info("acces token: " + ACCESS_TOKEN);
			CLIENT_TOKEN = jsonObject.getString("clientToken");
			Logger.info("client token: " + CLIENT_TOKEN);
			Logger.info("Connecté");
		}
	}

	private String sendPostRequest(String requestUrl, String payload) {
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8);
			writer.write(payload);
			writer.close();
			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuilder jsonString = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				jsonString.append(line);
			}
			br.close();
			connection.disconnect();

			return jsonString.toString();

		} catch (Exception e) {
			return null;
		}
	}
}
