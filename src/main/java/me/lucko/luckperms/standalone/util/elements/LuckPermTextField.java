package me.lucko.luckperms.standalone.util.elements;

import javafx.scene.control.TextField;
import me.lucko.luckperms.LPStandaloneApp;

public class LuckPermTextField extends TextField {

	public LuckPermTextField() {
		super();
		setFont(LPStandaloneApp.FONT);
	}

	public LuckPermTextField(String preFilled) {
		super(preFilled);
		setFont(LPStandaloneApp.FONT);
	}
}
