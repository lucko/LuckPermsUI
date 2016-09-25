package nl.makertim.luckpermsui.elements;

import javafx.scene.control.Label;
import nl.makertim.luckpermsui.MainWindow;

public class LuckPermLabel extends Label {

	public LuckPermLabel(String txt) {
		super(txt);
		setFont(MainWindow.FONT);
	}
}
