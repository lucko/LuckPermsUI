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

	public Collection<Permission> getPermissions() {
		Collection<Permission> ret = new HashSet<>();
		for (Map.Entry<String, JsonElement> elementEntry : getPerms().entrySet()) {
			String permissionName = elementEntry.getKey();
			if (permissionName.isEmpty()) {
				continue;
			}
			boolean active = elementEntry.getValue().getAsBoolean();
			Permission permission;
			if (permissionName.contains("/")) {
				String[] permissionSplit = permissionName.split("\\/", 2);
				String serverWorld = permissionSplit[0];
				String node = permissionSplit[1];
				if (serverWorld.contains("-")) {
					String[] serverWorldSplit = serverWorld.split("-", 2);
					String server = serverWorldSplit[0];
					String world = serverWorldSplit[1];
					permission = new Permission(server, world, node, active);
				} else {
					permission = new Permission(serverWorld, node, active);
				}
			} else {
				permission = new Permission(permissionName, active);
			}
			ret.add(permission);
		}
		return ret;
	}

	public String getJson() {
		return perms.toString();
	}

	public void setPermission(String key, boolean b) {
		getPerms().add(key, new JsonPrimitive(b));
	}

	public void setPermission(Permission perm) {
		removePermission(perm);
		setPermission(perm.getKey(), perm.isActive());
	}

	public void removePermission(Permission perm) {
		if (getPerms().has(perm.getKey())) {
			getPerms().remove(perm.getKey());
		}
	}

	public void removePermission(String key) {
		getPerms().remove(key);
	}

	@Override
	public String toString() {
		return name;
	}
}
