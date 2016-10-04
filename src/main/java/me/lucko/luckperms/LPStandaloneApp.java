package me.lucko.luckperms;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import lombok.Getter;
import me.lucko.luckperms.standalone.StandaloneBase;
import me.lucko.luckperms.standalone.factory.LoginFactory;

public class LPStandaloneApp extends Application {
	public static final Font FONT;

	static {
		Font font = Font.loadFont(LPStandaloneApp.class.getResourceAsStream("/assets/Ubuntu.ttf"), 12);
		FONT = font == null ? Font.font(12) : font;
	}

	private StandaloneBase base;

	@Getter
	private Stage primaryStage;

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;

		Pane login = LoginFactory.getInstance().create(primaryStage);

		Scene startScene = new Scene(login);
		primaryStage.setTitle("LuckPerms User Interface\u2122");
		primaryStage.setScene(startScene);
		primaryStage.show();
	}

	public void stop() {
		if (base == null) {
			return;
		}
		base.getLog().info("Closing datastore...");
		base.getDatastore().shutdown();

		base.getLog().info("Unregistering API...");
		LuckPerms.unregisterProvider();
	}

}
