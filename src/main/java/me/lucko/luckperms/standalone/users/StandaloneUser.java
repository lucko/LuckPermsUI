package me.lucko.luckperms.standalone.users;

import java.util.UUID;

import me.lucko.luckperms.LuckPermsPlugin;
import me.lucko.luckperms.users.User;

public class StandaloneUser extends User {

	StandaloneUser(UUID uuid, LuckPermsPlugin plugin) {
		super(uuid, plugin);
	}

	StandaloneUser(UUID uuid, String username, LuckPermsPlugin plugin) {
		super(uuid, username, plugin);
	}

	@Override
	public void refreshPermissions() {
		// Do nothing.
	}
}
