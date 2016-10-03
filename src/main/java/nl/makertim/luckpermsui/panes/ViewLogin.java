package nl.makertim.luckpermsui.panes;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import me.lucko.luckperms.LPStandaloneApp;
import nl.makertim.luckpermsui.DatabaseType;
import nl.makertim.luckpermsui.StorageOptions;
import nl.makertim.luckpermsui.elements.LuckPermLabel;
import nl.makertim.luckpermsui.elements.LuckPermTextField;

public class ViewLogin extends StackPane {
    private final LPStandaloneApp app;

	public ViewLogin(LPStandaloneApp app) {
        this.app = app;
		setup();
	}

	private void setup() {
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10, 10, 10, 10));

		int row = 0;
		Label databaseTypeLabel = new LuckPermLabel("Database Type");
		ComboBox<DatabaseType> databaseTypeField = new ComboBox<>();
		databaseTypeField.getItems().addAll(DatabaseType.values());
		GridPane.setConstraints(databaseTypeLabel, 1, row);
		GridPane.setConstraints(databaseTypeField, 2, row++);

		Label databaseHostLabel = new LuckPermLabel("Host");
		TextField databaseHostField = new LuckPermTextField();
		databaseHostField.setPromptText("example.com");
		GridPane.setConstraints(databaseHostLabel, 1, row);
		GridPane.setConstraints(databaseHostField, 2, row++);

		Label databasePortLabel = new LuckPermLabel("Port");
		TextField databasePortField = new LuckPermTextField();
		databasePortField.setPromptText("3306");
		GridPane.setConstraints(databasePortLabel, 1, row);
		GridPane.setConstraints(databasePortField, 2, row++);

		Label databaseLabel = new LuckPermLabel("Database");
		TextField databaseField = new LuckPermTextField();
		GridPane.setConstraints(databaseLabel, 1, row);
		GridPane.setConstraints(databaseField, 2, row++);

		Label usernameLabel = new LuckPermLabel("Username");
		TextField usernameField = new LuckPermTextField();
		usernameField.setPromptText("My Awesome Username");
		GridPane.setConstraints(usernameLabel, 1, row);
		GridPane.setConstraints(usernameField, 2, row++);

		Label passwordLabel = new LuckPermLabel("Password");
		TextField passwordField = new PasswordField();
		passwordField.setPromptText("My Secret Password");
		GridPane.setConstraints(passwordLabel, 1, row);
		GridPane.setConstraints(passwordField, 2, row++);

        // TODO Add different options for Flatfile / Mongo / H2 / SQLite
        // TODO Perhaps dynamically change the buttons here depending on which 'Database type' is chosen?

		Button loginButton = new Button("Login");
		loginButton.setOnAction((ActionEvent click) -> {
            DatabaseType type = databaseTypeField.getValue();
            if (type == DatabaseType.MYSQL) {
                int port = 0;
                try {
                    port = Integer.parseInt(databasePortField.getText());
                } catch (NumberFormatException e) {
                    System.err.println(databasePortField.getText() + " is not a number.");
                }

                setupBase(new StorageOptions(
                        "mysql",
                        databaseHostField.getText() + ":" + port,
                        databaseField.getText(),
                        usernameField.getText(),
                        passwordField.getText(),
                        null)
                );
            } else {
                // TODO Add other types
            }
        });

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

	private void setupBase(StorageOptions options) {
        app.setupBase(options);

		try {

			Stage stage = app.getPrimaryStage();
			stage.setScene(new Scene(new ViewManager(), 1024, 768));
			stage.setMinWidth(1000);
			stage.setMinHeight(768);
			stage.setMaxHeight(768);
		} catch (NullPointerException ex) {
			ex.printStackTrace();
		}
	}

}
