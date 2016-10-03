package me.lucko.luckperms.standalone.view.popup;

import java.util.Arrays;

import javafx.scene.control.TextField;
import me.lucko.luckperms.standalone.util.form.FormBase;
import me.lucko.luckperms.standalone.util.form.FormItem;
import me.lucko.luckperms.standalone.util.form.FormResultType;
import me.lucko.luckperms.standalone.view.scene.Manager;

public class UserNew extends FormBase {
	// TODO
	public UserNew(Manager view, String preFilled) {
		super(view, "Registering a new user into the system.",
				Arrays.asList(new FormItem("Username", new TextField(preFilled))),
				Arrays.asList(FormResultType.OK, FormResultType.CANCEL));
	}

}
