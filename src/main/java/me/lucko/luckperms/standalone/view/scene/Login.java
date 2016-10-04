package me.lucko.luckperms.standalone.view.scene;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import me.lucko.luckperms.DatabaseType;
import me.lucko.luckperms.api.data.Callback;
import me.lucko.luckperms.standalone.controller.LoginController;
import me.lucko.luckperms.standalone.model.StorageOptions;
import me.lucko.luckperms.standalone.util.elements.LuckPermLabel;
import me.lucko.luckperms.standalone.util.elements.LuckPermTextField;

public class Login extends VBox {

	private LoginController controller;
	private DatabaseForm form;

	public Login(LoginController controller) {
		super(8);
		setAlignment(Pos.TOP_CENTER);
		this.controller = controller;
		setup();
	}

	private void setup() {
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(8));

		HBox row = new HBox(8);
		row.setAlignment(Pos.CENTER);
		Label databaseTypeLabel = new LuckPermLabel("Database type");
		ComboBox<DatabaseType> databaseTypeField = new ComboBox<>();
		databaseTypeField.getItems().addAll(DatabaseType.values());
		row.getChildren().addAll(databaseTypeLabel, databaseTypeField);

		Button loginButton = new Button("Login");
		loginButton.setOnAction((ActionEvent click) -> {
			StorageOptions options = form.onConfirm();
			controller.startupManageView(options);
		});

		databaseTypeField.valueProperty().addListener(change -> {
			DatabaseType selected = databaseTypeField.getValue();
			if (selected == null) {
				return;
			}
			changeForm(selected, grid);
		});
		databaseTypeField.valueProperty().setValue(databaseTypeField.getItems().get(0));

		getChildren().addAll(row, grid, loginButton);
	}

	private void changeForm(DatabaseType type, GridPane grid) {
		grid.getChildren().clear();
		switch (type) {
		case MYSQL:
			form = new MySQLForm();
			break;
		case SQLITE:
			form = new SQLiteForm();
			break;
		case H2:
			form = new H2Form();
			break;
		case JSON:
			form = new JSONForm();
			break;
		case YAML:
			form = new YAMLForm();
			break;
		case MONGODB:
			form = new MongoDBForm();
			break;
		}
		form.build(grid);
	}

	public interface DatabaseForm {
		void build(GridPane pane);

		StorageOptions onConfirm();
	}

	public class MySQLForm extends LoginForm {
		@Override
		public DatabaseType getType() {
			return DatabaseType.MYSQL;
		}
	}

	public class SQLiteForm extends FileForm {
		@Override
		public DatabaseType getType() {
			return DatabaseType.SQLITE;
		}
	}

	public class H2Form extends FileForm {
		@Override
		public DatabaseType getType() {
			return DatabaseType.H2;
		}
	}

	public class JSONForm extends FileForm {
		@Override
		public DatabaseType getType() {
			return DatabaseType.JSON;
		}
	}

	public class YAMLForm extends FileForm {
		@Override
		public DatabaseType getType() {
			return DatabaseType.YAML;
		}
	}

	public class MongoDBForm extends LoginForm {
		@Override
		public DatabaseType getType() {
			return DatabaseType.MONGODB;
		}
	}

	public abstract class FileForm implements DatabaseForm {
		private File file;
		private TextField fileLocation;

		@Override
		public void build(GridPane pane) {
			int row = 0;
			Label fileLocationLabel = new LuckPermLabel("Location");
			fileLocation = new LuckPermTextField();
			fileLocation.setPromptText("path/to/file");
			GridPane.setConstraints(fileLocationLabel, 1, ++row);
			GridPane.setConstraints(fileLocation, 2, row);

			Button buttonBrowse = new Button("Browse...");
			buttonBrowse.setOnMouseClicked(click -> {
				chooseFile(newFile -> fileLocation.setText(newFile.getAbsolutePath()));
			});
			GridPane.setConstraints(buttonBrowse, 1, ++row, 2, 1);

			pane.getChildren().addAll(fileLocationLabel, fileLocation, buttonBrowse);
		}

		@Override
		public StorageOptions onConfirm() {
			return new StorageOptions(getType(), file);
		}

		private void chooseFile(Callback<File> fileCallback) {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Choose database file for type '" + getType().getType() + "'.");
			File file = fileChooser.showOpenDialog(Login.this.getScene().getWindow());
			if (file != null) {
				this.file = file;
				fileCallback.onComplete(file);
			}
		}

		public abstract DatabaseType getType();
	}

	public abstract class LoginForm implements DatabaseForm {
		private TextField databaseHostField;
		private TextField databasePortField;
		private TextField databaseField;
		private TextField usernameField;
		private TextField passwordField;

		@Override
		public void build(GridPane pane) {
			int row = 0;
			Label databaseHostLabel = new LuckPermLabel("Host");
			databaseHostField = new LuckPermTextField();
			databaseHostField.setPromptText("example.com");
			GridPane.setConstraints(databaseHostLabel, 1, ++row);
			GridPane.setConstraints(databaseHostField, 2, row);

			Label databasePortLabel = new LuckPermLabel("Port");
			databasePortField = new LuckPermTextField();
			databasePortField.setPromptText("3306");
			GridPane.setConstraints(databasePortLabel, 1, ++row);
			GridPane.setConstraints(databasePortField, 2, row);

			Label databaseLabel = new LuckPermLabel("Database");
			databaseField = new LuckPermTextField();
			GridPane.setConstraints(databaseLabel, 1, ++row);
			GridPane.setConstraints(databaseField, 2, row);

			Label usernameLabel = new LuckPermLabel("Username");
			usernameField = new LuckPermTextField();
			usernameField.setPromptText("My Awesome Username");
			GridPane.setConstraints(usernameLabel, 1, ++row);
			GridPane.setConstraints(usernameField, 2, row);

			Label passwordLabel = new LuckPermLabel("Password");
			passwordField = new PasswordField();
			passwordField.setPromptText("My Secret Password");
			GridPane.setConstraints(passwordLabel, 1, ++row);
			GridPane.setConstraints(passwordField, 2, row);

			pane.getChildren().addAll(databaseHostLabel, databaseHostField, databasePortLabel, databasePortField,
				databaseLabel, databaseField, usernameLabel, usernameField, passwordLabel, passwordField);
		}

		@Override
		public StorageOptions onConfirm() {
			return new StorageOptions(getType(), databaseHostField.getText() + ":" + databasePortField.getText(),
					databaseField.getText(), usernameField.getText(), passwordField.getText());
		}

		public abstract DatabaseType getType();
	}
}
