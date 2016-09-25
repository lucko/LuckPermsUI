package nl.makertim.luckpermsui.elements;

import javafx.scene.control.TextField;
import nl.makertim.luckpermsui.MainWindow;

public class LuckPermTextField extends TextField {

	public LuckPermTextField() {
		super();
		setFont(MainWindow.FONT);
	}

	public LuckPermTextField(String preFilled) {
		super(preFilled);
		setFont(MainWindow.FONT);
	}
}
