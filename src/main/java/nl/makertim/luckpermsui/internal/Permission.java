package nl.makertim.luckpermsui.internal;

import com.google.gson.JsonElement;

public class Permission {

	private JsonElement origin;
	private String server;
	private String world;
	private String node;

	public Permission(String node, JsonElement jsonElement) {
		this(null, null, node, jsonElement);
	}

	public Permission(String server, String node, JsonElement jsonElement) {
		this(server, null, node, jsonElement);
	}

	public Permission(String server, String world, String node, JsonElement jsonElement) {
		this.server = server;
		this.world = world;
		this.node = node;
		this.origin = jsonElement;
	}

	public boolean hasServer() {
		return getServer() != null;
	}

	public String getServer() {
		return server;
	}

	public boolean hasWorld() {
		return getWorld() != null;
	}

	public String getWorld() {
		return world;
	}

	public boolean hasNode() {
		return getNode() != null;
	}

	public String getNode() {
		return node;
	}

	public JsonElement getOrigin() {
		return origin;
	}

	@Override
	public String toString() {
		return String.format("%s-%s/%s", getServer(), getWorld(), getNode());
	}

	@Override
	public int hashCode() {
		return toString().hashCode() ^ 2;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Permission) {
			Permission other = ((Permission) obj);
			return (node == null ? other.node == null : node.equals(other.node))
					&& (world == null ? other.world == null : world.equals(other.world))
					&& (server == null ? other.server == null : server.equals(other.server));
		}
		return super.equals(obj);
	}
}
