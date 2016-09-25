package nl.makertim.luckpermsui.panes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import nl.makertim.luckpermsui.Main;
import nl.makertim.luckpermsui.elements.TexturedButton;
import nl.makertim.luckpermsui.form.FormBase;
import nl.makertim.luckpermsui.form.FormResultType;
import nl.makertim.luckpermsui.internal.Group;
import nl.makertim.luckpermsui.internal.GroupManager;

public class PaneGroup extends VBox {

	private ViewManager parent;
	private TextField search;
	private ListView<Group> groups;

	public PaneGroup(ViewManager viewManager) {
		setup();
		parent = viewManager;
	}

	private void setup() {
		// setPrefWidth(Short.MAX_VALUE);

		HBox topLine = new HBox();
		search = new TextField();
		search.setPromptText("Search group by name.");
		search.setMinWidth(300);
		search.setPrefWidth(540);
		TexturedButton addButton = new TexturedButton("assets/images/add.png", 24);
		TexturedButton removeButton = new TexturedButton("assets/images/remove.png", 24);

		groups = new ListView<>();
		groups.setPrefWidth(680);
		groups.setPrefHeight(704);
		fillGroups();

		search.textProperty().addListener(onChange -> fillGroups());
		groups.getSelectionModel().getSelectedItems().addListener((InvalidationListener) change -> {
			Group group = groups.getSelectionModel().getSelectedItem();
			if (group != null) {
				parent.setSideView(new SidePaneGroup(group));
			}
		});
		addButton.setOnMouseClicked((Consumer<MouseEvent>) click -> onNewGroup(search.getText()));
		removeButton.setOnMouseClicked(
			(Consumer<MouseEvent>) click -> onRemoveGroup(groups.getSelectionModel().getSelectedItem()));

		topLine.getChildren().addAll(search, addButton, removeButton);
		getChildren().addAll(topLine, groups);
	}

	private void onRemoveGroup(Group group) {
		if (group == null) {
			return;
		}
		FormBase form = new FormGroupDelete(parent, group);
		form.showForm(fr -> {
			if (fr.getType() == FormResultType.OK) {
				Object[] result = fr.getResult();
				if (result[0].equals(group.getName())) {
					GroupManager.removeGroup(group);
					fillGroups();
					parent.setSideView(null);
				} else {
					onRemoveGroup(group);
				}
			}
		});
	}

	private void onNewGroup(String preFilled) {
		FormBase form = new FormGroupNew(parent, preFilled);
		form.showForm(fr -> {
			if (fr.getType() == FormResultType.OK) {
				Object[] result = fr.getResult();
				String name = (String) result[0];
				Group group = GroupManager.createGroup(name);
				GroupManager.addGroup(group);
				fillGroups();
			}
		});
	}

	private void fillGroups() {
		groups.getItems().clear();
		String saveFilter = Main.manager.prepareString(search.getText());
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