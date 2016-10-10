package me.lucko.luckperms.standalone.view.elements;

import com.jfoenix.controls.JFXButton;
import javafx.scene.Node;

public class RaisedButton extends JFXButton {

	public RaisedButton() {
		setButtonType(ButtonType.RAISED);
	}

	public RaisedButton(String text) {
		super(text);
		setButtonType(ButtonType.RAISED);
		getStyleClass().add("button-raised");
	}

	public RaisedButton(String text, Node graphic) {
		super(text, graphic);
		setButtonType(ButtonType.RAISED);
	}
}
