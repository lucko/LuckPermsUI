package nl.makertim.luckpermsui.panes;

import me.lucko.luckperms.groups.Group;

public class FormPermissionAdd extends FormPermissionChange {

	public FormPermissionAdd(ViewManager view, Group group) {
		super(view, group, null, null, null, true);
	}

}
