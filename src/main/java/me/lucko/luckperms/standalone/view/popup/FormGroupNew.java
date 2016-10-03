package me.lucko.luckperms.standalone.view.popup;

import java.util.Arrays;

import me.lucko.luckperms.standalone.view.scene.ViewManager;
import me.lucko.luckperms.standalone.util.elements.LuckPermTextField;
import me.lucko.luckperms.standalone.util.form.FormBase;
import me.lucko.luckperms.standalone.util.form.FormItem;
import me.lucko.luckperms.standalone.util.form.FormResultType;

public class FormGroupNew extends FormBase {

	public FormGroupNew(ViewManager view, String preFilled) {
		super(view, "New Group", Arrays.asList(new FormItem("Name", new LuckPermTextField(preFilled))),
				Arrays.asList(FormResultType.OK, FormResultType.CANCEL));
	}
}
