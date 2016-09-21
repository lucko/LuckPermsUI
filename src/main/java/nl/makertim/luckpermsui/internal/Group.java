package nl.makertim.luckpermsui.internal;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Group {

	public String name;
	public JsonObject perms;

	public Group(String name, String perms) {
		this.name = name;
		this.perms = new JsonParser().parse(perms).getAsJsonObject();
	}

	public String getName() {
		return name;
	}

	public JsonObject getPerms() {
		return perms;
	}

	@Override
	public String toString() {
		return name;
	}
}
