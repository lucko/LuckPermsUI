package me.lucko.luckperms.standalone.view.centerpane;

import java.util.Map;
import java.util.regex.Pattern;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import me.lucko.luckperms.groups.Group;
import me.lucko.luckperms.groups.GroupManager;
import me.lucko.luckperms.standalone.factory.SimpleViewFactory;
import me.lucko.luckperms.standalone.view.elements.GroupTreeObject;
import me.lucko.luckperms.standalone.view.elements.TexturedButton;
import me.lucko.luckperms.standalone.view.scene.Manager;
import me.lucko.luckperms.standalone.view.sidepane.SideGroup;

public class GroupListManager extends VBox {

	private Manager parent;
	private TextField search;
	private TreeTableView<GroupTreeObject> groupList;

	private GroupManager groupManager;

	public GroupListManager(Manager manager) {
		parent = manager;
		groupManager = parent.getController().getBase().getGroupManager();
		setup();
	}

	private void setup() {
		HBox topLine = new HBox();
		search = new JFXTextField();
		search.setPromptText("Search group by name.");
		search.setPrefWidth(Short.MAX_VALUE);
		TexturedButton addButton = new TexturedButton("assets/images/add.png", 24, "Create a new group.");
		TexturedButton refreshButton = new TexturedButton("assets/images/refresh.png", 24, "Remove selected group.");
		TexturedButton removeButton = new TexturedButton("assets/images/remove.png", 24, "Remove selected group.");

		setupGroupView();
		fillGroupView(search.getText());

		search.textProperty().addListener(change -> fillGroupView(search.getText()));
		groupList.getSelectionModel().selectedIndexProperty().addListener(change -> updateSelected());

		topLine.getChildren().addAll(search, addButton, refreshButton, removeButton);
		getChildren().addAll(topLine, groupList);
	}

	private void updateSelected() {
		Group group = fromItem(groupList.getSelectionModel().getSelectedItem());
		if (group == null) {
			parent.setSideView(null);
			return;
		}
		parent.setSideView(SimpleViewFactory.getInstance().linkGroup(group, parent, parent.getController().getBase()));
	}

	private void setupGroupView() {
		groupList = new JFXTreeTableView<>();

		TreeTableColumn<GroupTreeObject, String> nameColumn = new JFXTreeTableColumn<>("Name");
		nameColumn.setPrefWidth(150);
		nameColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<GroupTreeObject, String> param) -> {
			Group group = fromItem(param.getValue());
			if (group == null) {
				return new SimpleStringProperty();
			}
			return new SimpleStringProperty(group.getDisplayName());
		});
		groupList.getColumns().add(nameColumn);
		TreeItem treeItem = new TreeItem<>();
		treeItem.setExpanded(true);
		groupList.setRoot(treeItem);
		groupList.setShowRoot(false);
		groupList.setPrefHeight(Short.MAX_VALUE);
	}

	private void fillGroupView(String filter) {
		Map<String, Group> groups = groupManager.getAll();
		groupList.getRoot().getChildren().clear();
		groups.values().stream().filter(group -> {
			String filterLower = filter.toLowerCase();
			Pattern pattern = Pattern.compile(".+");
			try {
				pattern = Pattern.compile(filterLower);
			} catch (Exception ex) {
			}
			String groupName = group.getDisplayName().toLowerCase();
			return filterLower.isEmpty() || pattern.matcher(groupName).find() || groupName.contains(filterLower);
		}).forEach(group -> groupList.getRoot().getChildren().add(new TreeItem<>(new GroupTreeObject(group))));
	}

	private Group fromItem(TreeItem<GroupTreeObject> treeItem) {
		TreeItem<GroupTreeObject> item = treeItem;
		if (item == null) {
			return null;
		}
		GroupTreeObject gto = item.getValue();
		if (gto == null) {
			return null;
		}
		return gto.getGroup();
	}

}
