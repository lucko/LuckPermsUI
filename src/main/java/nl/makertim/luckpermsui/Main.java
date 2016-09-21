package nl.makertim.luckpermsui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.makertim.luckpermsui.panes.LoginPane;
import nl.makertim.luckpermsui.util.IDatabaseManager;
import nl.makertim.luckpermsui.util.MySQLDatabaseManager;

public class Main {

	public static IDatabaseManager manager;
	public static String version = "2.8";

	public static void main(String[] args) {
		Application.launch(MainWindow.class, args);
	}

}
