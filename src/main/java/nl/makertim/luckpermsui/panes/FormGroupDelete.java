package nl.makertim.luckpermsui.panes;

import java.util.Arrays;

import javafx.scene.control.TextField;
import nl.makertim.luckpermsui.form.FormBase;
import nl.makertim.luckpermsui.form.FormItem;
import nl.makertim.luckpermsui.form.FormResultType;
import nl.makertim.luckpermsui.internal.Group;

public class FormGroupDelete extends FormBase {

	public FormGroupDelete(ViewManager view, Group group) {
		super(view, "DELETE GROUP",
				Arrays.asList(new FormItem("Confirm with: '" + group.getName() + "'", new TextField())),
				Arrays.asList(FormResultType.OK, FormResultType.CANCEL));
	}
}
