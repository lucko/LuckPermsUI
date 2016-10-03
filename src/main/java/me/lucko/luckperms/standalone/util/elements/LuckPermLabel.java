package me.lucko.luckperms.standalone.util.elements;

import javafx.scene.control.Label;
import me.lucko.luckperms.LPStandaloneApp;

public class LuckPermLabel extends Label {

	public LuckPermLabel() {
		this("");
	}

	public LuckPermLabel(String txt) {
		super(txt);
		setFont(LPStandaloneApp.FONT);
	}
}
