package nl.makertim.luckpermsui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import nl.makertim.luckpermsui.panes.ViewLogin;

public class MainWindow extends Application {

	private static MainWindow view;
	public static final Font FONT = Font.loadFont(Main.class.getResourceAsStream("/assets/Ubuntu.ttf"), 12);

	private Stage primaryStage;

	public void start(Stage primaryStage) {
		MainWindow.view = this;
		this.primaryStage = primaryStage;

		Scene login = new Scene(new ViewLogin());
		primaryStage.setTitle("LuckPerms Unofficial User Interface\u2122");
		primaryStage.setScene(login);
		primaryStage.show();
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static MainWindow getView() {
		return view;
	}
}
