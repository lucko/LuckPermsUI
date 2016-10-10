package me.lucko.luckperms.standalone.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.lucko.luckperms.api.Node;
import me.lucko.luckperms.exceptions.ObjectAlreadyHasException;
import me.lucko.luckperms.exceptions.ObjectLacksException;
import me.lucko.luckperms.groups.Group;
import me.lucko.luckperms.standalone.StandaloneBase;
import me.lucko.luckperms.standalone.util.form.FormBase;
import me.lucko.luckperms.standalone.util.form.FormResultType;
import me.lucko.luckperms.standalone.view.popup.PermissionAdd;
import me.lucko.luckperms.standalone.view.popup.PermissionChange;
import me.lucko.luckperms.standalone.view.popup.PermissionRemove;
import me.lucko.luckperms.standalone.view.scene.Manager;
import me.lucko.luckperms.standalone.view.sidepane.SideGroup;

@AllArgsConstructor
public class GroupController {

    @Getter
    private SideGroup view;

    @Getter
    private StandaloneBase model;

    public void addPermission(Manager parent, Group group) {
        FormBase form = new PermissionAdd(parent, group);
        form.showForm(fr -> {
            if (fr.getType() == FormResultType.OK) {
                Object[] values = fr.getResult();
                String node = (String) values[0];
                String server = (String) values[1];
                String world = (String) values[2];
                boolean active = (boolean) values[3];
                if (node.isEmpty()) {
                    return;
                }
                if (server == null || server.isEmpty()) {
                    server = null;
                    world = null;
                }
                if (world == null || world.isEmpty()) {
                    world = null;
                }
                /*
                 * Permission perm = new Permission(server, world, node,
				 * active); group.setPermission(perm);
				 * GroupListManager.updatePermissions(group);
				 */
                // TODO
                view.fillPermissionList();
            }
        });
    }

    public void changePermission(Manager parent, Group group, Node permission) {
        FormBase form = new PermissionChange(parent, group, permission);
        form.showForm(fr -> {
            if (fr.getType() == FormResultType.OK) {
                Object[] values = fr.getResult();
                String node = (String) values[0];
                String server = (String) values[1];
                String world = (String) values[2];
                boolean active = (boolean) values[3];
                if (node.isEmpty()) {
                    return;
                }
                if (server == null || server.isEmpty()) {
                    server = null;
                    world = null;
                }
                if (world == null || world.isEmpty()) {
                    world = null;
                }
                try {
                    group.unsetPermission(permission);
                } catch (ObjectLacksException ignored) {

                }

                Node perm = new me.lucko.luckperms.core.Node.Builder(node).setServer(server).setWorld(world)
                        .setValue(active).build();
                try {
                    group.setPermission(perm);
                } catch (ObjectAlreadyHasException e) {
                    e.printStackTrace();
                }
                // TODO save
                view.fillPermissionList();
            }
        });
    }

    public void removePermission(Manager parent, Group group, Node permission) {
        FormBase form = new PermissionRemove(parent, group, permission);
        form.showForm(fr -> {
            if (fr.getType() == FormResultType.YES) {
                // TODO remove & update permission
                view.fillPermissionList();
            }
        });
    }
}
