package nl.makertim.luckpermsui.util;

import java.net.URL;
import java.util.UUID;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class MojangAPI {

	public static UUID getUUIDFromName(String name) {
		try {
			String array = String.format("[\"%s\"]", name);
			String[] jsonUUID = WebUtil.getPostLines(new URL("https://api.mojang.com/profiles/minecraft"), array,
				con -> con.setRequestProperty("content-type", "application/json"));
			StringBuilder sb = new StringBuilder();
			for (String s : jsonUUID) {
				sb.append(s);
			}
			JsonArray ja = new JsonParser().parse(sb.toString()).getAsJsonArray();
			if (ja.size() > 0) {
				JsonObject person = ja.get(0).getAsJsonObject();
				String uuid = person.get("id").getAsString();
				return UUID.fromString(String.format("%s-%s-%s-%s-%s", uuid.substring(0, 8), uuid.substring(8, 12),
					uuid.substring(12, 16), uuid.substring(16, 20), uuid.substring(20, 32)));
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getNameFromUUID(UUID uuid) {
		try {
			String jsonUUID = WebUtil.getResponse(
				new URL(" https://api.mojang.com/user/profiles/" + uuid.toString().replaceAll("-", "") + "/names"));
			JsonArray ja = new JsonParser().parse(jsonUUID).getAsJsonArray();
			if (ja.size() > 0) {
				JsonObject person = ja.get(ja.size() - 1).getAsJsonObject();
				return person.get("name").getAsString();
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
