package nl.makertim.luckpermsui.form;

import java.util.List;

import nl.makertim.luckpermsui.panes.ViewManager;

public class FormRawBase extends FormBase {

	public FormRawBase(ViewManager parent, String name, List<FormItem> items, List<FormResultType> buttons) {
		super(parent, name, items, buttons);
	}

	@Override
	protected Object[] getValues() {
		Object[] ret = new Object[controls.length];
		for (int i = 0; i < controls.length; i++) {
			ret[i] = controls[i];
		}
		return ret;
	}
}
