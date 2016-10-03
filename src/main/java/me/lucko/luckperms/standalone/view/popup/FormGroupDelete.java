package me.lucko.luckperms.standalone.view.popup;

import java.util.Arrays;

import me.lucko.luckperms.groups.Group;
import me.lucko.luckperms.standalone.view.scene.ViewManager;
import me.lucko.luckperms.standalone.util.elements.LuckPermTextField;
import me.lucko.luckperms.standalone.util.form.FormBase;
import me.lucko.luckperms.standalone.util.form.FormItem;
import me.lucko.luckperms.standalone.util.form.FormResultType;

public class FormGroupDelete extends FormBase {

	public FormGroupDelete(ViewManager view, Group group) {
		super(view, "DELETE GROUP " + group.getName(),
				Arrays.asList(new FormItem("Confirm with: '" + group.getName() + "'", new LuckPermTextField())),
				Arrays.asList(FormResultType.OK, FormResultType.CANCEL));
	}
}
