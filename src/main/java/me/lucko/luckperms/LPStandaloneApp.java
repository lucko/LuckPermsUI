package me.lucko.luckperms;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import lombok.Getter;
import me.lucko.luckperms.standalone.StandaloneBase;
import nl.makertim.luckpermsui.StorageOptions;
import nl.makertim.luckpermsui.panes.ViewLogin;

/**
 * Entry point
 *
 * Has to be in me.lucko.luckperms to hook with API
 */
public class LPStandaloneApp extends Application {
    public static final Font FONT = Font.loadFont(LPStandaloneApp.class.getResourceAsStream("/assets/Ubuntu.ttf"), 12);
    public static final Font MONOSPACE = Font.loadFont(LPStandaloneApp.class.getResourceAsStream("/assets/Letter Gothic.ttf"), 12);

    private StandaloneBase base;

    @Getter
    private Stage primaryStage;

    public static void main(String[] args) {
        new LPStandaloneApp().start(args);
    }

    public void start(String[] args) {
        Application.launch(args);
    }

    public void setupBase(StorageOptions options) {
        base = new StandaloneBase(this, options);
        // TODO register API provider, or not??
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        Scene login = new Scene(new ViewLogin(this));
        primaryStage.setTitle("LuckPerms User Interface\u2122");
        primaryStage.setScene(login);
        primaryStage.show();
    }

    public void stop() {

    }

}
