package nl.makertim.luckpermsui;

import javafx.application.Application;
import nl.makertim.luckpermsui.util.IDatabaseManager;

public class Main {

	public static IDatabaseManager manager;
	public static String version = "2.8";

	public static void main(String[] args) {
		Application.launch(MainWindow.class, args);
	}

}
