package nl.makertim.luckpermsui.panes;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import nl.makertim.luckpermsui.DatabaseType;
import nl.makertim.luckpermsui.Main;
import nl.makertim.luckpermsui.MainWindow;
import nl.makertim.luckpermsui.util.MySQLDatabaseManager;

public class LoginPane extends StackPane {

	public LoginPane() {
		setup();
	}

	private void setup() {
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10, 10, 10, 10));

		int row = 0;
		Label databaseTypeLabel = new Label("Database Type");
		ComboBox<DatabaseType> databaseTypeField = new ComboBox<>();
		databaseTypeField.getItems().addAll(DatabaseType.values());
		GridPane.setConstraints(databaseTypeLabel, 1, row);
		GridPane.setConstraints(databaseTypeField, 2, row++);

		Label databaseHostLabel = new Label("Host");
		TextField databaseHostField = new TextField();
		databaseHostField.setPromptText("example.com");
		GridPane.setConstraints(databaseHostLabel, 1, row);
		GridPane.setConstraints(databaseHostField, 2, row++);

		Label databasePortLabel = new Label("Port");
		TextField databasePortField = new TextField();
		databasePortField.setPromptText("3306");
		GridPane.setConstraints(databasePortLabel, 1, row);
		GridPane.setConstraints(databasePortField, 2, row++);

		Label databaseLabel = new Label("Database");
		TextField databaseField = new TextField();
		GridPane.setConstraints(databaseLabel, 1, row);
		GridPane.setConstraints(databaseField, 2, row++);

		Label usernameLabel = new Label("Username");
		TextField usernameField = new TextField();
		usernameField.setPromptText("My Awesome Username");
		GridPane.setConstraints(usernameLabel, 1, row);
		GridPane.setConstraints(usernameField, 2, row++);

		Label passwordLabel = new Label("Password");
		TextField passwordField = new PasswordField();
		passwordField.setPromptText("My Secret Password");
		GridPane.setConstraints(passwordLabel, 1, row);
		GridPane.setConstraints(passwordField, 2, row++);

		Button loginButton = new Button("Login");
		loginButton.setOnAction((ActionEvent click) -> onConnect(databaseTypeField.getValue(),
			databaseHostField.getText(), databasePortField.getText(), databaseField.getText(), usernameField.getText(),
			passwordField.getText()));
		databaseTypeField.valueProperty().addListener(change -> {
			DatabaseType selected = databaseTypeField.getValue();
			if (selected == DatabaseType.MYSQL || selected == DatabaseType.MONGODB) {
				databaseHostLabel.setText("Host");
				databaseHostField.setPromptText("example.com");
				databasePortField.setDisable(false);
				databaseField.setDisable(false);
				usernameField.setDisable(false);
				passwordField.setDisable(false);
			} else {
				databaseHostLabel.setText("File");
				databaseHostField.setPromptText("/usr/database.db");
				databasePortField.setDisable(true);
				databaseField.setDisable(true);
				usernameField.setDisable(true);
				passwordField.setDisable(true);
			}
		});
		GridPane.setConstraints(loginButton, 2, row);

		grid.getChildren().addAll(databaseTypeLabel, databaseTypeField, databaseHostLabel, databaseHostField,
			databasePortLabel, databasePortField, databaseLabel, databaseField, usernameLabel, usernameField,
			passwordLabel, passwordField, loginButton);
		this.getChildren().add(grid);
	}

	private void onConnect(DatabaseType type, String host, String _port, String database, String username, String password) {
		int port = 0;
		try {
			port = Integer.parseInt(_port);
		} catch (NumberFormatException e) {
			System.err.println(_port + " is not a number.");
		}
		switch (type) {
		case MYSQL:
			Main.manager = new MySQLDatabaseManager(host, port, username, password, database);
			break;
		case SQLITE:
			// TODO: remove debug
			Main.manager = new MySQLDatabaseManager("localhost", 3306, "stream", "", "test");
			break;
		case H2:
			break;
		case FLATFILE:
			break;
		case MONGODB:
			break;
		}
		try {
			if (!Main.manager.openConnection()) {
				return;
			}
			MainWindow.getView().getPrimaryStage().setScene(new Scene(new MultiView(), 600, 300));
		} catch (NullPointerException ex) {
			ex.printStackTrace();
		}
	}

}
