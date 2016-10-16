package me.lucko.luckperms;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import me.lucko.luckperms.standalone.StandaloneBase;
import me.lucko.luckperms.standalone.factory.SimpleViewFactory;

public class LPStandaloneApp extends Application {
	public static final Font FONT;

	static {
		Font font = Font.loadFont(LPStandaloneApp.class.getResourceAsStream("/assets/Ubuntu.ttf"), 12);
		FONT = font == null ? Font.font(12) : font;
	}

	@Setter
	private StandaloneBase base;

	@Getter
	private Stage primaryStage;

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;

		SimpleViewFactory.getInstance().openLogin(this);
		primaryStage.setTitle("LuckPerms User Interface\u2122");
		primaryStage.show();
	}

	public void setPrimaryScene(Scene scene) {
		scene.getStylesheets().add(getClass().getResource("/assets/style.css").toExternalForm());
		scene.getStylesheets().add(getClass().getResource("/resources/css/jfoenix-design.css").toExternalForm());
		primaryStage.setScene(scene);
	}

	public void stop() {
		if (base == null) {
			return;
		}
		base.getLog().info("Closing datastore...");
		base.shutdown();

		base.getLog().info("Unregistering API...");
		LuckPerms.unregisterProvider();
	}

}
