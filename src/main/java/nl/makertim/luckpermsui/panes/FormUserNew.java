package nl.makertim.luckpermsui.panes;

import java.util.Arrays;

import javafx.scene.control.TextField;
import nl.makertim.luckpermsui.form.FormBase;
import nl.makertim.luckpermsui.form.FormItem;
import nl.makertim.luckpermsui.form.FormResultType;

public class FormUserNew extends FormBase {
	// TODO
	public FormUserNew(ViewManager view, String preFilled) {
		super(view, "Registering a new user into the system.",
				Arrays.asList(new FormItem("Username", new TextField(preFilled))),
				Arrays.asList(FormResultType.OK, FormResultType.CANCEL));
	}

}
