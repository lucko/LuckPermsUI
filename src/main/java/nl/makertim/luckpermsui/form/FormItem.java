package nl.makertim.luckpermsui.form;

import javafx.scene.control.Control;

public class FormItem {

	private String name;
	private Control item;

	public FormItem(String name, Control item) {
		this.name = name;
		this.item = item;
	}

	public String getName() {
		return name;
	}

	public Control getItem() {
		return item;
	}
}
