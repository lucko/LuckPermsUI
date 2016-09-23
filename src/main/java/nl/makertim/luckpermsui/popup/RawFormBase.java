package nl.makertim.luckpermsui.popup;

import java.util.List;

import javafx.stage.Window;

public class RawFormBase extends FormBase {

	public RawFormBase(Window parent, String name, List<FormItem> items, List<FormResultType> buttons) {
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
