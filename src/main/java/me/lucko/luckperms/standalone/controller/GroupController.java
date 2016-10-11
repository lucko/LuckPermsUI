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
import me.lucko.luckperms.standalone.util.form.Updatable;
import me.lucko.luckperms.standalone.view.popup.*;
import me.lucko.luckperms.standalone.view.scene.Manager;

@AllArgsConstructor
public class GroupController {

	@Getter
	private Updatable view;

	@Getter
	private StandaloneBase base;

	public void addGroup(Manager parent) {
		FormBase form = new GroupNew(parent, "");
		form.showForm(fr -> {
			if (fr.getType() == FormResultType.OK) {
				Object[] values = fr.getResult();
				String name = (String) values[0];
				if (name.isEmpty() || base.getDatastore().loadGroup(name)) {
					return;
				}

				base.getDatastore().createAndLoadGroup(name);
				view.update();
			}
		});
	}

	public void removeGroup(Manager parent, Group group) {
		FormBase form = new GroupDelete(parent, group);
		form.showForm(fr -> {
			if (fr.getType() == FormResultType.OK) {
				Object[] values = fr.getResult();
				String name = (String) values[0];
				if (group.getDisplayName().equals(name)) {
					base.getDatastore().deleteGroup(group);
					view.update();
				} else {
					removeGroup(parent, group);
				}
			}
		});
	}

	public void refresh(StandaloneBase base) {
		base.reloadGroups();
	}

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

				addPermission(group, node, server, world, active);
				base.getDatastore().saveGroup(group);
				view.update();
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

				removePermission(group, permission);
				addPermission(group, node, server, world, active);
				base.getDatastore().saveGroup(group);
				view.update();
			}
		});
	}

	public void removePermission(Manager parent, Group group, Node permission) {
		FormBase form = new PermissionRemove(parent, group, permission);
		form.showForm(fr -> {
			if (fr.getType() == FormResultType.YES) {

				removePermission(group, permission);
				base.getDatastore().saveGroup(group);
				view.update();
			}
		});
	}

	private Group addGroup(String name) {
		Group newGroup = base.getGroupManager().apply(name);
		return newGroup;
	}

	private void removePermission(Group group, Node permission) {
		try {
			group.unsetPermission(permission);
		} catch (ObjectLacksException ex) {
			ex.printStackTrace();
		}
	}

	private void addPermission(Group group, String node, String server, String world, boolean active) {
		Node perm = new me.lucko.luckperms.core.Node.Builder(node).setServer(server).setWorld(world).setValue(active)
				.build();
		try {
			group.setPermission(perm);
		} catch (ObjectAlreadyHasException ex) {
			ex.printStackTrace();
		}
	}
}
