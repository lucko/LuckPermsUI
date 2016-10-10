package me.lucko.luckperms.standalone.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.lucko.luckperms.LPStandaloneApp;
import me.lucko.luckperms.standalone.StandaloneBase;
import me.lucko.luckperms.standalone.factory.SimpleViewFactory;
import me.lucko.luckperms.standalone.model.StorageOptions;
import me.lucko.luckperms.storage.Datastore;
import me.lucko.luckperms.storage.methods.*;

@AllArgsConstructor
public class LoginController {

	@Getter
	private LPStandaloneApp app;

	public void startupManageView(StorageOptions options) {
		StandaloneBase base = new StandaloneBase(options);
		Datastore datastore;
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
		default:
			datastore = null;
		}
		base.doAsync(() -> {
			base.loadDatastore(datastore);
			base.doSync(() -> SimpleViewFactory.getInstance().openManager(app, base));
		});
	}

}
