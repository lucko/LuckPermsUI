package me.lucko.luckperms.standalone.controller;

import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import me.lucko.luckperms.LPStandaloneApp;
import me.lucko.luckperms.standalone.StandaloneBase;
import me.lucko.luckperms.standalone.model.StorageOptions;
import me.lucko.luckperms.standalone.view.scene.Manager;
import me.lucko.luckperms.storage.Datastore;
import me.lucko.luckperms.storage.methods.*;

public class LoginController {

	@Getter
	private LPStandaloneApp app;

	public LoginController(LPStandaloneApp app) {
		this.app = app;
	}

	public void startupManageView(StorageOptions options) {
		StandaloneBase base = new StandaloneBase(options);
		Datastore datastore = null;
		switch (options.getType()) {
		case MYSQL:
			datastore = new MySQLDatastore(base, options);
			break;
		case SQLITE:
			datastore = new SQLiteDatastore(base, options.getFile());
			break;
		case H2:
			datastore = new H2Datastore(base, options.getFile());
			break;
		case JSON:
			datastore = new JSONDatastore(base, options.getFile());
			break;
		case YAML:
			datastore = new YAMLDatastore(base, options.getFile());
			break;
		case MONGODB:
			datastore = new MongoDBDatastore(base, options);
			break;
		}
		base.loadDatastore(datastore);

		try {
			Stage stage = app.getPrimaryStage();
			stage.setScene(new Scene(new Manager(), 1024, 768));
			stage.setMinWidth(1000);
			stage.setMinHeight(768);
			stage.setMaxHeight(768);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
