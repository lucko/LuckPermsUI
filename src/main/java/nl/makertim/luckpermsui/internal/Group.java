package nl.makertim.luckpermsui.internal;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

public class Group {

	private String name;
	private JsonObject perms;

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

	public Collection<String> getPermissions() {
		Collection<String> ret = new HashSet<>();
		for (Map.Entry<String, JsonElement> elementEntry : getPerms().entrySet()) {
			if (elementEntry.getValue().isJsonPrimitive() && elementEntry.getValue().getAsBoolean()) {
				String permissionName = elementEntry.getKey();
				ret.add(permissionName);
			}
		}
		return ret;
	}

	public String getJson() {
		return perms.toString();
	}

	public void addPermission(String key) {
		getPerms().add(key, new JsonPrimitive(true));
	}

	@Override
	public String toString() {
		return name;
	}
}
