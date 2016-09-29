package nl.makertim.luckpermsui.internal;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

public class Group {

	private final String name;
	private final JsonObject perms;
	private final String[] inherit;
	private final String prefix;

	public Group(String name, String perms) {
		this.name = name;
		this.perms = new JsonParser().parse(perms).getAsJsonObject();
		List<String> inheritList = this.perms.entrySet().stream()
				.filter(permission -> permission.getKey().startsWith("group."))
				.map(permission -> permission.getKey().replaceAll("group\\.", "")).collect(Collectors.toList());
		inherit = inheritList.toArray(new String[inheritList.size()]);
		List<String> prefixes = this.perms.entrySet().stream()
				.filter(permission -> permission.getKey().startsWith("prefix."))
				.map(permission -> permission.getKey().replaceAll("prefix.", "")).collect(Collectors.toList());
		prefixes.sort((String perm1, String perm2) -> {
			perm1 = perm1.substring(0, perm1.indexOf("."));
			perm2 = perm2.substring(0, perm2.indexOf("."));
			int perm1Value = Integer.parseInt(perm1);
			int perm2Value = Integer.parseInt(perm2);
			return Integer.compare(perm2Value, perm1Value);
		});
		if (prefixes.size() > 0) {
			String prefix = prefixes.get(0);
			this.prefix = prefix.substring(prefix.indexOf(".") + 1);
		} else {
			this.prefix = "";
		}
	}

	public String getName() {
		return name;
	}

	public JsonObject getPerms() {
		return perms;
	}

	public String[] getInherit() {
		return inherit;
	}

	public String getPrefix() {
		return prefix;
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
			if (permission.getKey().startsWith("group.") || permission.getKey().startsWith("prefix.")) {
				continue;
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
