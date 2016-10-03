package me.lucko.luckperms.standalone.users;

import me.lucko.luckperms.LuckPermsPlugin;
import me.lucko.luckperms.users.User;
import me.lucko.luckperms.users.UserIdentifier;
import me.lucko.luckperms.users.UserManager;

public class StandaloneUserManager extends UserManager {
    private final LuckPermsPlugin plugin;

    public StandaloneUserManager(LuckPermsPlugin plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @Override
    public void cleanup(User user) {
        // unload(user);
        // never unload????? TODO
    }

    @Override
    public User apply(UserIdentifier id) {
        StandaloneUser user = id.getUsername() == null ?
                new StandaloneUser(id.getUuid(), plugin) :
                new StandaloneUser(id.getUuid(), id.getUsername(), plugin);
        giveDefaultIfNeeded(user, false);
        return user;
    }

    @Override
    public void updateAllUsers() {
        getAll().values().forEach(u -> plugin.getDatastore().loadUser(u.getUuid(), u.getName()));
    }
}
