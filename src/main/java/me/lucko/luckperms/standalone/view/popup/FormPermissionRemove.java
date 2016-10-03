package me.lucko.luckperms.standalone.view.popup;

import java.util.ArrayList;
import java.util.Arrays;

import me.lucko.luckperms.api.Node;
import me.lucko.luckperms.groups.Group;
import me.lucko.luckperms.standalone.view.scene.ViewManager;
import me.lucko.luckperms.standalone.util.form.FormBase;
import me.lucko.luckperms.standalone.util.form.FormResultType;

public class FormPermissionRemove extends FormBase {

	public FormPermissionRemove(ViewManager view, Group group, Node perm) {
		super(view, "Are you sure to remove the permission " + perm.getPermission() + " from " + group.getName() + "?",
				new ArrayList<>(), Arrays.asList(FormResultType.YES, FormResultType.NO));
	}
}
