package me.lucko.luckperms.standalone.view.scene;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.lucko.luckperms.api.data.Callback;
import me.lucko.luckperms.standalone.DatabaseType;
import me.lucko.luckperms.standalone.controller.LoginController;
import me.lucko.luckperms.standalone.model.StorageOptions;
import me.lucko.luckperms.standalone.view.elements.RaisedButton;

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

		Label databaseTypeLabel = new Label("Database type");
		ComboBox<DatabaseType> databaseTypeField = new JFXComboBox<>();
		databaseTypeField.getItems().addAll(DatabaseType.values());
		row.getChildren().addAll(databaseTypeLabel, databaseTypeField);

		Button loginButton = new RaisedButton("Login");
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
			case MONGODB:
			case MYSQL:
				form = new LoginForm(type);
				break;
			case SQLITE:
			case H2:
				form = new FileForm(type);
				break;
			case JSON:
			case YAML:
				form = new FolderForm(type);
				break;
		}
		form.build(grid);
	}

	public interface DatabaseForm {

		void build(GridPane pane);
		StorageOptions onConfirm();

	}

	public class FolderForm extends AbstractFileForm {
		public FolderForm(DatabaseType type) {
			super(type, "path/to/data/folder");
		}

		@Override
		protected void chooseFile(Callback<File> fileCallback) {
			DirectoryChooser directoryChooser = new DirectoryChooser();
			directoryChooser.setTitle("Choose database folder for type '" + getType().getType() + "'.");
			File file = directoryChooser.showDialog(Login.this.getScene().getWindow());
			if (file != null) {
				this.file = file;
				fileCallback.onComplete(file);
			}
		}
	}

	public class FileForm extends AbstractFileForm {
		public FileForm(DatabaseType type) {
			super(type, "path/to/file");
		}

		@Override
		protected void chooseFile(Callback<File> fileCallback) {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Choose database file for type '" + getType().getType() + "'.");
			File file = fileChooser.showOpenDialog(Login.this.getScene().getWindow());
			if (file != null) {
				this.file = file;
				fileCallback.onComplete(file);
			}
		}
	}

	@RequiredArgsConstructor
	public abstract class AbstractFileForm implements DatabaseForm {

		@Getter
		private final DatabaseType type;
		private final String pathDescriptor;

		protected File file;
		private TextField fileLocation;

		@Override
		public void build(GridPane pane) {
			int row = 0;
			Label fileLocationLabel = new Label("Location");
			fileLocation = new JFXTextField();
			fileLocation.setPromptText(pathDescriptor);
			GridPane.setConstraints(fileLocationLabel, 1, ++row);
			GridPane.setConstraints(fileLocation, 2, row);

			Button buttonBrowse = new RaisedButton("Browse...");
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

		protected abstract void chooseFile(Callback<File> fileCallback);
	}

	@RequiredArgsConstructor
	public class LoginForm implements DatabaseForm {

		@Getter
		private final DatabaseType type;

		private TextField databaseHostField;
		private TextField databasePortField;
		private TextField databaseField;
		private TextField usernameField;
		private TextField passwordField;

		@Override
		public void build(GridPane pane) {
			int row = 0;
			Label databaseHostLabel = new Label("Host");
			databaseHostField = new JFXTextField();
			databaseHostField.setPromptText("127.0.0.1");
			GridPane.setConstraints(databaseHostLabel, 1, ++row);
			GridPane.setConstraints(databaseHostField, 2, row);

			Label databasePortLabel = new Label("Port");
			databasePortField = new JFXTextField();
			databasePortField.setPromptText("3306");
			GridPane.setConstraints(databasePortLabel, 1, ++row);
			GridPane.setConstraints(databasePortField, 2, row);

			Label databaseLabel = new Label("Database");
			databaseField = new JFXTextField();
			databaseField.setPromptText("minecraft");
			GridPane.setConstraints(databaseLabel, 1, ++row);
			GridPane.setConstraints(databaseField, 2, row);

			Label usernameLabel = new Label("Username");
			usernameField = new JFXTextField();
			usernameField.setPromptText("root");
			GridPane.setConstraints(usernameLabel, 1, ++row);
			GridPane.setConstraints(usernameField, 2, row);

			Label passwordLabel = new Label("Password");
			passwordField = new JFXPasswordField();
			passwordField.setPromptText("passw0rd");
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
	}
}
