package me.lucko.luckperms.standalone.util;

import java.net.URL;
import java.util.UUID;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class UsernameGetter {

	public static String currentName(UUID uuid) {
		try {
			URL mojangAPI = new URL(
					"https://api.mojang.com/user/profiles/" + uuid.toString().replaceAll("-", "") + "/names");
			String jsonResponse = WebUtil.getResponse(mojangAPI);
			JsonArray json = new JsonParser().parse(jsonResponse).getAsJsonArray();
			JsonObject lastName = json.get(0).getAsJsonObject();
			return lastName.get("name").getAsString();
		} catch (Exception ex) {
			System.err.println(ex.toString());
			return null;
		}
	}
}
