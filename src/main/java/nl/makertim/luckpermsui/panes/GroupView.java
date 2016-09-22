package nl.makertim.luckpermsui.panes;

import javafx.application.Platform;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import nl.makertim.luckpermsui.Main;
import nl.makertim.luckpermsui.internal.Group;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupView extends VBox {

	MultiView parent;

	public GroupView(MultiView multiView) {
		setup();
		parent = multiView;
	}

	private void setup() {
		setPrefWidth(500);

		HBox topLine = new HBox();
		TextField search = new TextField();
		search.setPromptText("Search group by name.");
		search.setPrefWidth(340);

		ListView<Group> groups = new ListView<>();
		groups.setPrefWidth(680);
		groups.setPrefHeight(744);
		fillGroups(groups, search.getText());

		search.textProperty().addListener(onChange -> fillGroups(groups, search.getText()));
		groups.setOnMouseClicked(
			click -> parent.setSideView(new GroupSideView(groups.getSelectionModel().getSelectedItem())));

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
