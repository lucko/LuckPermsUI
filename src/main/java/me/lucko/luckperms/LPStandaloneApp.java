package me.lucko.luckperms;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import lombok.Getter;
import me.lucko.luckperms.standalone.StandaloneBase;
import me.lucko.luckperms.standalone.controller.LoginController;
import me.lucko.luckperms.standalone.model.StorageOptions;
import me.lucko.luckperms.standalone.view.scene.ViewLogin;

public class LPStandaloneApp extends Application {
    public static final Font FONT = Font.loadFont(LPStandaloneApp.class.getResourceAsStream("/assets/Ubuntu.ttf"), 12);
    public static final Font MONOSPACE = Font.loadFont(LPStandaloneApp.class.getResourceAsStream("/assets/Letter Gothic.ttf"), 12);

    private StandaloneBase base;

    @Getter
    private Stage primaryStage;

    public static void main(String[] args) {
        Application.launch(args);
    }

    public void setupBase(StorageOptions options) {

        // TODO register API provider, or not??
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

		LoginController controller = new LoginController(this);
		ViewLogin login = new ViewLogin(controller);

		Scene startScene = new Scene(login);
        primaryStage.setTitle("LuckPerms User Interface\u2122");
		primaryStage.setScene(startScene);
        primaryStage.show();
    }

    public void stop() {

    }

}
