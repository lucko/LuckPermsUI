package nl.makertim.luckpermsui.internal;

public class Permission {

	private String server;
	private String world;
	private String node;
	private boolean active;

	public Permission(String node, boolean active) {
		this(null, null, node, active);
	}

	public Permission(String server, String node, boolean active) {
		this(server, null, node, active);
	}

	public Permission(String server, String world, String node, boolean active) {
		this.server = server;
		this.world = world;
		this.node = node;
		this.active = active;
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

	public boolean isActive() {
		return active;
	}

	public String getKey() {
		if (hasServer()) {
			if (hasWorld()) {
				return String.format("%s-%s/%s", getServer(), getWorld(), getNode());
			} else {
				return String.format("%s/%s", getServer(), getNode());
			}
		}
		return String.format("%s", getNode());
	}

	@Override
	public String toString() {
		return getKey();
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
