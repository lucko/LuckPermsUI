package me.lucko.luckperms.standalone.view.popup;

import me.lucko.luckperms.groups.Group;
import me.lucko.luckperms.standalone.view.scene.ViewManager;

public class FormPermissionAdd extends FormPermissionChange {

	public FormPermissionAdd(ViewManager view, Group group) {
		super(view, group, null, null, null, true);
	}

}
