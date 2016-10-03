package me.lucko.luckperms.standalone.view.popup;

import java.util.ArrayList;
import java.util.Arrays;

import me.lucko.luckperms.api.Node;
import me.lucko.luckperms.groups.Group;
import me.lucko.luckperms.standalone.util.form.FormBase;
import me.lucko.luckperms.standalone.util.form.FormResultType;
import me.lucko.luckperms.standalone.view.scene.Manager;

public class PermissionRemove extends FormBase {

	public PermissionRemove(Manager view, Group group, Node perm) {
		super(view, "Are you sure to remove the permission " + perm.getPermission() + " from " + group.getName() + "?",
				new ArrayList<>(), Arrays.asList(FormResultType.YES, FormResultType.NO));
	}
}
