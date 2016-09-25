package nl.makertim.luckpermsui.panes;

import java.util.Arrays;

import nl.makertim.luckpermsui.elements.LuckPermTextField;
import nl.makertim.luckpermsui.form.FormBase;
import nl.makertim.luckpermsui.form.FormItem;
import nl.makertim.luckpermsui.form.FormResultType;

public class FormGroupNew extends FormBase {

	public FormGroupNew(ViewManager view, String preFilled) {
		super(view, "New Group", Arrays.asList(new FormItem("Name", new LuckPermTextField(preFilled))),
				Arrays.asList(FormResultType.OK, FormResultType.CANCEL));
	}
}
