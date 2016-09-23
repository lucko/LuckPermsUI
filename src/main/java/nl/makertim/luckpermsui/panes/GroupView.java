package nl.makertim.luckpermsui.panes;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import nl.makertim.luckpermsui.Main;
import nl.makertim.luckpermsui.elements.TexturedButton;
import nl.makertim.luckpermsui.internal.Group;
import nl.makertim.luckpermsui.internal.GroupManager;
import nl.makertim.luckpermsui.popup.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;

public class GroupView extends VBox {

	private MultiView parent;
	private TextField search;
	private ListView<Group> groups;

	public GroupView(MultiView multiView) {
		setup();
		parent = multiView;
	}

	private void setup() {
		setPrefWidth(500);

		HBox topLine = new HBox();
		search = new TextField();
		search.setPromptText("Search group by name.");
		search.setPrefWidth(340);
		TexturedButton addButton = new TexturedButton("assets/images/add.png", 24);
		TexturedButton removeButton = new TexturedButton("assets/images/remove.png", 24);

		groups = new ListView<>();
		groups.setPrefWidth(680);
		groups.setPrefHeight(744);
		fillGroups();

		search.textProperty().addListener(onChange -> fillGroups());
		groups.setOnMouseClicked(
			click -> parent.setSideView(new GroupSideView(groups.getSelectionModel().getSelectedItem())));
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
		FormBase form = new FormBase(getScene().getWindow(), "DELETE GROUP",
				Arrays.asList(new FormItem("Confirm with: '" + group.getName() + "'", new TextField())),
				Arrays.asList(FormResultType.OK, FormResultType.CANCEL));
		FormResult fr = form.showForm();
		if (fr.getType() == FormResultType.OK) {
			Object[] result = fr.getResult();
			String confirm = (String) result[0];
			if (confirm.equals(group.getName())) {
				GroupManager.removeGroup(group);
				fillGroups();
			} else {
				onRemoveGroup(group);
			}
		}

	}

	private void onNewGroup(String preFilled) {
		FormBase form = new FormBase(getScene().getWindow(), "New Group",
				Arrays.asList(new FormItem("Name", new TextField(preFilled))),
				Arrays.asList(FormResultType.OK, FormResultType.CANCEL));
		FormResult fr = form.showForm();
		if (fr.getType() == FormResultType.OK) {
			Object[] result = fr.getResult();
			String name = (String) result[0];
			Group group = GroupManager.createGroup(name);
			GroupManager.addGroup(group);
			fillGroups();
		}
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
