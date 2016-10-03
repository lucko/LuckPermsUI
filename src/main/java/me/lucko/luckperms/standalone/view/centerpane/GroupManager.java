package me.lucko.luckperms.standalone.view.centerpane;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import me.lucko.luckperms.groups.Group;
import me.lucko.luckperms.standalone.util.elements.LuckPermTextField;
import me.lucko.luckperms.standalone.util.elements.TexturedButton;
import me.lucko.luckperms.standalone.util.form.FormBase;
import me.lucko.luckperms.standalone.util.form.FormResultType;
import me.lucko.luckperms.standalone.view.popup.GroupDelete;
import me.lucko.luckperms.standalone.view.popup.GroupNew;
import me.lucko.luckperms.standalone.view.scene.Manager;
import me.lucko.luckperms.standalone.view.sidepane.SideGroup;

public class GroupManager extends VBox {

	private Manager parent;
	private TextField search;
	private TableView<Group> groups;

	public GroupManager(Manager manager) {
		parent = manager;
		setup();
	}

	private void setup() {
		HBox topLine = new HBox();
		search = new LuckPermTextField();
		search.setPromptText("Search group by name.");
		search.setPrefWidth(Short.MAX_VALUE);
		TexturedButton addButton = new TexturedButton("assets/images/add.png", 24, "Create a new group.");
		TexturedButton refreshButton = new TexturedButton("assets/images/refresh.png", 24, "Remove selected group.");
		TexturedButton removeButton = new TexturedButton("assets/images/remove.png", 24, "Remove selected group.");

		setupTableView();
		fillGroups();

		search.textProperty().addListener(onChange -> fillGroups());
		groups.getSelectionModel().getSelectedItems().addListener((InvalidationListener) change -> {
			Group group = groups.getSelectionModel().getSelectedItem();
			if (group != null) {
				parent.setSideView(new SideGroup(parent, group));
			}
		});
		addButton.setOnMouseClicked((Consumer<MouseEvent>) click -> onNewGroup(search.getText()));
		refreshButton.setOnMouseClicked((Consumer<MouseEvent>) click -> fillGroups());
		removeButton.setOnMouseClicked(
			(Consumer<MouseEvent>) click -> onRemoveGroup(groups.getSelectionModel().getSelectedItem()));

		topLine.getChildren().addAll(search, addButton, refreshButton, removeButton);
		getChildren().addAll(topLine, groups);
	}

	private void onRemoveGroup(Group group) {
		if (group == null) {
			return;
		}
		FormBase form = new GroupDelete(parent, group);
		form.showForm(fr -> {
			if (fr.getType() == FormResultType.OK) {
				Object[] result = fr.getResult();
				if (result[0].equals(group.getName())) {
					// TODO delete
					fillGroups();
					parent.setSideView(null);
				} else {
					onRemoveGroup(group);
				}
			}
		});
	}

	private void onNewGroup(String preFilled) {
		FormBase form = new GroupNew(parent, preFilled);
		form.showForm(fr -> {
			if (fr.getType() == FormResultType.OK) {
				Object[] result = fr.getResult();
				String name = (String) result[0];
				// todo create & add
				fillGroups();
			}
		});
	}

	private void setupTableView() {
		groups = new TableView<>();
		groups.setPrefHeight(704);

		TableColumn name = new TableColumn("Name");
		name.setCellValueFactory(new PropertyValueFactory<Group, UUID>("name"));
		TableColumn count = new TableColumn("Users in group");
		count.setCellValueFactory(cvf -> {
			TableColumn.CellDataFeatures cdf = (TableColumn.CellDataFeatures) cvf;
			ObservableValue ret = new SimpleIntegerProperty(1); // TODO
																// GroupManager.countUsersInGroup((Group)
																// cdf.getValue())
			return ret;
		});

		groups.getColumns().addAll(name, count);
	}

	private void fillGroups() {
		groups.getItems().clear();
		parent.setSideView(null);
		List<Group> groups = null; // TODO
		this.groups.getItems().addAll(groups);
	}

}
