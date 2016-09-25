package nl.makertim.luckpermsui.panes;

import java.util.ArrayList;
import java.util.Arrays;

import nl.makertim.luckpermsui.form.FormBase;
import nl.makertim.luckpermsui.form.FormResultType;
import nl.makertim.luckpermsui.internal.Group;
import nl.makertim.luckpermsui.internal.Permission;

public class FormPermissionRemove extends FormBase {

	public FormPermissionRemove(ViewManager view, Group group, Permission perm) {
		super(view, "Are you sure to remove the permission " + perm.getNode() + " from " + group.getName() + "?",
				new ArrayList<>(), Arrays.asList(FormResultType.YES, FormResultType.NO));
	}
}
