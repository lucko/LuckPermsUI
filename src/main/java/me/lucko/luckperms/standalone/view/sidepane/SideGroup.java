package me.lucko.luckperms.standalone.view.sidepane;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;

import java.util.function.Function;
import java.util.regex.Pattern;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Setter;
import me.lucko.luckperms.api.MetaUtils;
import me.lucko.luckperms.api.Node;
import me.lucko.luckperms.common.api.internal.GroupLink;
import me.lucko.luckperms.common.groups.Group;
import me.lucko.luckperms.standalone.LPStandaloneApp;
import me.lucko.luckperms.standalone.controller.GroupController;
import me.lucko.luckperms.standalone.util.ColoredLine;
import me.lucko.luckperms.standalone.util.form.Updatable;
import me.lucko.luckperms.standalone.view.elements.NodeTreeObject;
import me.lucko.luckperms.standalone.view.elements.TexturedButton;
import me.lucko.luckperms.standalone.view.scene.Manager;

/**
 * The Sidebar for groups
 * TODO javadocs
 */
public class SideGroup extends VBox implements Updatable {

	private TreeTableView<NodeTreeObject> permissionList;
	private TextField searchServer;
	private TextField searchWorld;
	private TextField searchNode;
	private VBox groupInfo;

	private Manager parent;
	private Group group;

	@Setter
	private GroupController controller;

	public SideGroup(Manager parent, Group group) {
		super();
		this.parent = parent;
		this.group = group;
		setup();
	}

	private void setup() {
		BorderPane topLine = new BorderPane();
		HBox topRightCorner = new HBox();
		Label nameLabel = new Label(String.format("Permissions of %s.", group.getName()));
		nameLabel.setPadding(new Insets(4, 0, 0, 0));

		TexturedButton addButton = new TexturedButton("assets/images/add.png", 24, String.format("Add a permission to the group %s.", group.getName()));
		TexturedButton changeButton = new TexturedButton("assets/images/change.png", 24, "Change selected permission.");
		TexturedButton removeButton = new TexturedButton("assets/images/remove.png", 24, String.format("Remove selected permission for %s.", group.getName()));

		HBox searchers = new HBox();
		searchServer = new JFXTextField();
		searchServer.setPromptText("Search filter server.");

		searchWorld = new JFXTextField();
		searchWorld.setPromptText("Search filter world.");

		searchNode = new JFXTextField();
		searchNode.setPromptText("Search filter node.");

		groupInfo = new VBox(1);
		groupInfo.setPadding(new Insets(3, 0, 3, 3));

		buildGroupInfo();
		setupTable();

		addButton.setOnMouseClicked(click -> onPermissionAdd());
		changeButton.setOnMouseClicked(click -> onPermissionChange(fromItem(permissionList.getSelectionModel().getSelectedItem())));
		removeButton.setOnMouseClicked(click -> onPermissionRemove(fromItem(permissionList.getSelectionModel().getSelectedItem())));

		searchServer.textProperty().addListener(onChange -> populatePermissionList());
		searchWorld.textProperty().addListener(onChange -> populatePermissionList());
		searchNode.textProperty().addListener(onChange -> populatePermissionList());
		permissionList.setOnMouseClicked(click -> {
			if (click.getClickCount() >= 2) {
				onPermissionChange(fromItem(permissionList.getSelectionModel().getSelectedItem()));
			}
		});

		topRightCorner.getChildren().addAll(addButton, changeButton, removeButton);
		topLine.setLeft(nameLabel);
		topLine.setRight(topRightCorner);
		searchers.getChildren().addAll(searchServer, searchWorld, searchNode);
		getChildren().addAll(topLine, groupInfo, searchers, permissionList);
	}

	private void buildGroupInfo() {
		groupInfo.getChildren().clear();

		String prefix = MetaUtils.getPrefix(new GroupLink(group), null, null, true);
		if (prefix != null && !prefix.isEmpty()) {
			ColoredLine prefixLine = new ColoredLine("Prefix = " + prefix);
			prefixLine.setFont(LPStandaloneApp.FONT);
			groupInfo.getChildren().add(prefixLine);
		} else {
			groupInfo.getChildren().add(new Label("No prefix"));
		}

		if (group.getGroupNames().size() > 0) {
			groupInfo.getChildren().add(new Label("Inherits from:"));
			for (String parentGroup : group.getGroupNames()) {
				groupInfo.getChildren().add(new Label("  " + parentGroup));
			}
		} else {
			groupInfo.getChildren().add(new Label("No inherited groups"));
		}
	}

	private TreeTableColumn<NodeTreeObject, String> makeColumn(String name, Function<Node, SimpleStringProperty> propFunc) {
		TreeTableColumn<NodeTreeObject, String> column = new JFXTreeTableColumn<>(name);
		column.setResizable(false);
		column.setCellValueFactory((TreeTableColumn.CellDataFeatures<NodeTreeObject, String> param) -> {
			Node node = fromItem(param.getValue());
			if (node == null) {
				return new SimpleStringProperty();
			}
			return propFunc.apply(node);
		});
		return column;
	}

	private void setupTable() {
		permissionList = new JFXTreeTableView<>();
		permissionList.getColumns().add(makeColumn("Node", node -> new SimpleStringProperty(node.getPermission())));
		permissionList.getColumns().add(makeColumn("Value", node -> new SimpleStringProperty(node.getValue().toString())));
		permissionList.getColumns().add(makeColumn("Server", node -> new SimpleStringProperty(node.getServer().orElse(""))));
		permissionList.getColumns().add(makeColumn("World", node -> new SimpleStringProperty(node.getWorld().orElse(""))));

		TreeItem<NodeTreeObject> treeItem = new TreeItem<>();
		treeItem.setExpanded(true);
		permissionList.setRoot(treeItem);
		permissionList.setShowRoot(false);
		permissionList.setPrefHeight(Short.MAX_VALUE);

		// TODO doc? wtf does this do?!!
		widthProperty().addListener(change -> {
			int hack = 3;
			int columns = permissionList.getColumns().size() + hack;
			for (TreeTableColumn column : permissionList.getColumns()) {
				column.setPrefWidth((getWidth() - 4) / columns * (hack + 1));
				hack = 0;
			}
		});

		populatePermissionList();
		permissionList.setPrefWidth(250);
		permissionList.setPrefHeight(Short.MAX_VALUE);
	}

	private void onPermissionAdd() {
		if (group == null) {
			return;
		}
		controller.addPermission(parent, group);
	}

	private void onPermissionChange(Node permission) {
		if (group == null || permission == null) {
			return;
		}
		controller.changePermission(parent, group, permission);
	}

	private void onPermissionRemove(Node permission) {
		if (group == null || permission == null) {
			return;
		}
		controller.removePermission(parent, group, permission);
	}

	public void populatePermissionList() {
		permissionList.getRoot().getChildren().clear();

		String searchServer = this.searchServer.getText();
		String searchWorld = this.searchWorld.getText();
		String searchNode = this.searchNode.getText();

		Pattern serverPattern = null;
		Pattern worldPattern = null;
		Pattern nodePattern = null;

		try {
			serverPattern = Pattern.compile(searchServer);
			worldPattern = Pattern.compile(searchWorld);
			nodePattern = Pattern.compile(searchNode);
		} catch (Exception ignored) {}

		for (Node permission : group.getNodes()) {
			if (!searchNode.isEmpty()
					&& (permission.getPermission() == null || !permission.getPermission().contains(searchNode)
							&& (nodePattern != null && !nodePattern.matcher(permission.getPermission()).find()))) {
				continue;
			}
			if (!searchWorld.isEmpty()
					&& (permission.getWorld() == null || !permission.getWorld().get().contains(searchWorld)
							&& (worldPattern != null && !worldPattern.matcher(permission.getWorld().get()).find()))) {
				continue;
			}
			if (!searchServer.isEmpty() && (permission.getServer() == null
					|| !permission.getServer().get().contains(searchServer) && (serverPattern != null
							&& !serverPattern.matcher(permission.getServer().get()).find()))) {
				continue;
			}

			permissionList.getRoot().getChildren().add(new TreeItem<>(new NodeTreeObject(permission)));
		}
	}

	@Override
	public void update() {
		buildGroupInfo();
		populatePermissionList();
	}

	private Node fromItem(TreeItem<NodeTreeObject> item) {
		if (item == null) {
			return null;
		}

		NodeTreeObject nto = item.getValue();
		if (nto == null) {
			return null;
		}

		return nto.getNode();
	}
}
