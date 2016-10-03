package me.lucko.luckperms.standalone.controller;

import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import me.lucko.luckperms.LPStandaloneApp;
import me.lucko.luckperms.LuckPermsPlugin;
import me.lucko.luckperms.standalone.StandaloneBase;
import me.lucko.luckperms.standalone.model.StorageOptions;
import me.lucko.luckperms.standalone.view.scene.ViewManager;

public class LoginController {

	@Getter
	private LPStandaloneApp app;

	public LoginController(LPStandaloneApp app) {
		this.app = app;
	}

	public void startupManageView(StorageOptions options) {
		// TODO: open manager
		LuckPermsPlugin base = new StandaloneBase(options);

		try {
			Stage stage = app.getPrimaryStage();
			stage.setScene(new Scene(new ViewManager(), 1024, 768));
			stage.setMinWidth(1000);
			stage.setMinHeight(768);
			stage.setMaxHeight(768);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
