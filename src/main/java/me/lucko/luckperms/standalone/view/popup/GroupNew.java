package me.lucko.luckperms.standalone.view.popup;

import java.util.Arrays;
import java.util.Collections;

import com.jfoenix.controls.JFXTextField;

import me.lucko.luckperms.standalone.util.form.FormBase;
import me.lucko.luckperms.standalone.util.form.FormItem;
import me.lucko.luckperms.standalone.util.form.FormResultType;
import me.lucko.luckperms.standalone.view.scene.Manager;

public class GroupNew extends FormBase {

	public GroupNew(Manager view, String preFilled) {
		super(view, "New Group",
				Collections.singletonList(new FormItem("Name", new JFXTextField(preFilled))),
				Arrays.asList(FormResultType.OK, FormResultType.CANCEL)
		);
	}
}
