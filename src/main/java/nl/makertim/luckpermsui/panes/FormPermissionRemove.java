package nl.makertim.luckpermsui.panes;

import java.util.ArrayList;
import java.util.Arrays;

import me.lucko.luckperms.api.Node;
import me.lucko.luckperms.groups.Group;
import nl.makertim.luckpermsui.form.FormBase;
import nl.makertim.luckpermsui.form.FormResultType;

public class FormPermissionRemove extends FormBase {

	public FormPermissionRemove(ViewManager view, Group group, Node perm) {
		super(view, "Are you sure to remove the permission " + perm.getPermission() + " from " + group.getName() + "?",
				new ArrayList<>(), Arrays.asList(FormResultType.YES, FormResultType.NO));
	}
}
