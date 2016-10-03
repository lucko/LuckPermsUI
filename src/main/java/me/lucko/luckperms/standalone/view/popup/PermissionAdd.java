package me.lucko.luckperms.standalone.view.popup;

import me.lucko.luckperms.groups.Group;
import me.lucko.luckperms.standalone.view.scene.Manager;

public class PermissionAdd extends PermissionChange {

	public PermissionAdd(Manager view, Group group) {
		super(view, group, null, null, null, true);
	}

}
