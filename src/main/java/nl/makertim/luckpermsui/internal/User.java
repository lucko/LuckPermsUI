package nl.makertim.luckpermsui.internal;

import java.util.UUID;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class User {

    private UUID uuid;
    private String name;
    private String defaultGroup;
    private JsonObject perms;

    public User(UUID uuid, String name, String defaultGroup, String perms) {
        this.uuid = uuid;
        this.name = name;
        this.defaultGroup = defaultGroup;
        this.perms = new JsonParser().parse(perms).getAsJsonObject();
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefaultGroup() {
        return defaultGroup;
    }

    public void setDefaultGroup(Group defaultGroup) {
        this.defaultGroup = defaultGroup.getName();
    }

    public JsonObject getPerms() {
        return perms;
    }

    public String getJson() {
        return perms.toString();
    }

    @Override
    public String toString() {
        return String.format("[%s] %s.", getUuid(), getName());
    }


}
