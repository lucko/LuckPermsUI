package nl.makertim.luckpermsui.panes;

import java.util.Arrays;

import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import nl.makertim.luckpermsui.form.FormBase;
import nl.makertim.luckpermsui.form.FormItem;
import nl.makertim.luckpermsui.form.FormResultType;
import nl.makertim.luckpermsui.internal.Group;

public class FormPermissionAdd extends FormBase {

	public FormPermissionAdd(ViewManager view, Group group) {
		super(view, "Add permission for group: " + group.getName(), Arrays.asList(
			new FormItem("Permission", new TextField()), new FormItem("Server [optional]", new TextField()),
			new FormItem("World [optional]", new TextField()), new FormItem("Allow permission?", new CheckBox())),
				Arrays.asList(FormResultType.OK, FormResultType.CANCEL));
	}
}
