package nl.makertim.luckpermsui.panes;

import javafx.application.Platform;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import nl.makertim.luckpermsui.Main;
import nl.makertim.luckpermsui.internal.Group;
import nl.makertim.luckpermsui.popup.GroupPopup;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupView extends VBox {

	public GroupView() {
		setup();
	}

	private void setup() {
		setPrefWidth(500);

		HBox topLine = new HBox();
		TextField search = new TextField();
		search.setPromptText("Search group by name");

		ListView<Group> groups = new ListView<>();
		fillGroups(groups, search.getText());

		search.textProperty().addListener(onChange -> fillGroups(groups, search.getText()));
		groups.setOnMouseClicked(click -> {
			if (click.getClickCount() >= 2) {
				new GroupPopup(groups.getSelectionModel().getSelectedItem()).showAndWait();
			}
		});

		topLine.getChildren().addAll(search);
		getChildren().addAll(topLine, groups);
	}

	private void fillGroups(ListView<Group> groups, String filter) {
		groups.getItems().clear();
		String saveFilter = Main.manager.prepareString(filter);
		new Thread(() -> {
			ResultSet rs = Main.manager
					.selectQuery("SELECT * FROM lp_groups WHERE name LIKE '%" + saveFilter + "%' ORDER BY name;");
			try {
				while (rs.next()) {
					String name = rs.getString("name");
					String perms = rs.getString("perms");
					Platform.runLater(() -> groups.getItems().add(new Group(name, perms)));
				}
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}).start();
	}

}
