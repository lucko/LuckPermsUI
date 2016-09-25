package nl.makertim.luckpermsui.panes;

import java.util.Arrays;

import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import nl.makertim.luckpermsui.form.FormBase;
import nl.makertim.luckpermsui.form.FormItem;
import nl.makertim.luckpermsui.form.FormResultType;
import nl.makertim.luckpermsui.internal.Group;
import nl.makertim.luckpermsui.internal.Permission;

public class FormPermissionChange extends FormBase {

	public FormPermissionChange(ViewManager view, Group group, String node, String server, String world, boolean active) {
		super(view, "Add permission for group: " + group.getName(),
				Arrays.asList(new FormItem("Permission", new TextField(node == null ? "" : node)),
					new FormItem("Server [optional]", new TextField(server == null ? "" : server)),
					new FormItem("World [optional]", new TextField(world == null ? "" : world)),
					new FormItem("Allow permission?", preDoneCheckbox(active))),
				Arrays.asList(FormResultType.OK, FormResultType.CANCEL));
	}

	public FormPermissionChange(ViewManager view, Group group, Permission perms) {
		this(view, group, perms.getNode(), perms.getServer(), perms.getWorld(), perms.isActive());
	}

	public static CheckBox preDoneCheckbox(boolean selected) {
		CheckBox checkBox = new CheckBox();
		checkBox.setSelected(selected);
		return checkBox;
	}
}
