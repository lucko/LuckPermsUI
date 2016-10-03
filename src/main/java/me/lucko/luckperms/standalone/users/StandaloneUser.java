package me.lucko.luckperms.standalone.users;

import me.lucko.luckperms.LuckPermsPlugin;
import me.lucko.luckperms.users.User;

import java.util.UUID;

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
