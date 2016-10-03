package nl.makertim.luckpermsui.elements;

import javafx.scene.control.Label;
import me.lucko.luckperms.LPStandaloneApp;

public class LuckPermLabel extends Label {

	public LuckPermLabel(String txt) {
		super(txt);
		setFont(LPStandaloneApp.FONT);
	}
}
