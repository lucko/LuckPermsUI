package nl.makertim.luckpermsui.panes;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
import nl.makertim.luckpermsui.elements.*;
import nl.makertim.luckpermsui.form.FormBase;
import nl.makertim.luckpermsui.form.FormResultType;
import nl.makertim.luckpermsui.internal.Group;
import nl.makertim.luckpermsui.internal.GroupManager;
import nl.makertim.luckpermsui.internal.Permission;
import nl.makertim.luckpermsui.internal.User;

public class SidePaneGroup extends VBox {

	private ViewManager parent;
	private Group group;
	private TableView<Permission> permissionList;
	TextField searchServer;
	TextField searchWorld;
	TextField searchNode;

	public SidePaneGroup(ViewManager parent, Group group) {
		super();
		this.parent = parent;
		this.group = group;
		setup();
	}

	private void setup() {
		BorderPane topLine = new BorderPane();
		HBox topRightCorner = new HBox();
		Label nameLabel = new LuckPermLabel(String.format("Permissions of %s.", group.getName()));
		nameLabel.setPadding(new Insets(4, 0, 0, 0));
		TexturedButton addButton = new TexturedButton("assets/images/add.png", 24,
				String.format("Add a permission to the group %s.", group.getName()));
		TexturedButton changeButton = new TexturedButton("assets/images/change.png", 24, "Change selected permission.");
		TexturedButton removeButton = new TexturedButton("assets/images/remove.png", 24,
				String.format("Remove selected permission.", group.getName()));

		HBox searchers = new HBox();
		searchServer = new LuckPermTextField();
		searchServer.setPromptText("Search filter server.");
		searchWorld = new LuckPermTextField();
		searchWorld.setPromptText("Search filter world.");
		searchNode = new LuckPermTextField();
		searchNode.setPromptText("Search filter node.");

		VBox groupInfo = new VBox(1);
		groupInfo.setPadding(new Insets(3, 0, 3, 3));
		if (!group.getPrefix().isEmpty()) {
			groupInfo.getChildren().add(new LuckPermLabel("Prefix = " + group.getPrefix()));
		}
		if (group.getInherit().length > 0) {
			groupInfo.getChildren().add(new LuckPermLabel("Inherits from:"));
			for (String parentGroup : group.getInherit()) {
				groupInfo.getChildren().add(new LuckPermLabel("  " + parentGroup));
			}
		}

		setupTable();

		addButton.setOnMouseClicked((Consumer<MouseEvent>) click -> onPermissionAdd());
		changeButton.setOnMouseClicked(
			(Consumer<MouseEvent>) click -> onPermissionChange(permissionList.getSelectionModel().getSelectedItem()));
		removeButton.setOnMouseClicked(
			(Consumer<MouseEvent>) click -> onPermissionRemove(permissionList.getSelectionModel().getSelectedItem()));

		searchServer.textProperty().addListener(onChange -> fillPermissionList());
		searchWorld.textProperty().addListener(onChange -> fillPermissionList());
		searchNode.textProperty().addListener(onChange -> fillPermissionList());

		topRightCorner.getChildren().addAll(addButton, changeButton, removeButton);
		topLine.setLeft(nameLabel);
		topLine.setRight(topRightCorner);
		searchers.getChildren().addAll(searchServer, searchWorld, searchNode);
		getChildren().addAll(topLine, searchers, groupInfo, permissionList);

		Platform.runLater(() -> {
			searchServer.setPrefWidth(Short.MAX_VALUE);
			searchWorld.setPrefWidth(Short.MAX_VALUE);
			searchNode.setPrefWidth(Short.MAX_VALUE);
		});
	}

	private void setupTable() {
		permissionList = new TableView<>();
		permissionList.setRowFactory(rf -> new TableRow<Permission>() {
            @Override
            protected void updateItem(Permission permission, boolean empty) {
                if (permission == null) {
                    setTextFill(Color.BLACK);
                } else if (permission.isActive()) {
                    setTextFill(Color.GREEN);
                } else {
                    setTextFill(Color.RED);
                }
            }
        });

		TableColumn node = new TableColumn("Node");
		node.setCellValueFactory(new PropertyValueFactory<Permission, UUID>("node"));
		TableColumn server = new TableColumn("Server");
		server.setCellValueFactory(new PropertyValueFactory<Permission, UUID>("server"));
		TableColumn world = new TableColumn("World");
		world.setCellValueFactory(new PropertyValueFactory<Permission, UUID>("world"));
		TableColumn active = new TableColumn("Allowed");
		active.setCellValueFactory(new PropertyValueFactory<Permission, UUID>("active"));

		permissionList.getColumns().addAll(node, server, world, active);

		fillPermissionList();
		permissionList.setPrefWidth(250);
		permissionList.setPrefHeight(687);
	}

	private void onPermissionAdd() {
		if (group == null) {
			return;
		}
		FormBase form = new FormPermissionAdd(parent, group);
		form.showForm(fr -> {
			if (fr.getType() == FormResultType.OK) {
				Object[] values = fr.getResult();
				String node = (String) values[0];
				String server = (String) values[1];
				String world = (String) values[2];
				boolean active = (boolean) values[3];
				if (node.isEmpty()) {
					return;
				}
				if (server == null || server.isEmpty()) {
					server = null;
					world = null;
				}
				if (world == null || world.isEmpty()) {
					world = null;
				}
				Permission perm = new Permission(server, world, node, active);
				group.setPermission(perm);
				GroupManager.updatePermissions(group);
				fillPermissionList();
			}
		});
	}

	private void onPermissionChange(Permission permission) {
		if (group == null || permission == null) {
			return;
		}
		FormBase form = new FormPermissionChange(parent, group, permission);
		form.showForm(fr -> {
			if (fr.getType() == FormResultType.OK) {
				Object[] values = fr.getResult();
				String node = (String) values[0];
				String server = (String) values[1];
				String world = (String) values[2];
				boolean active = (boolean) values[3];
				if (node.isEmpty()) {
					return;
				}
				if (server == null || server.isEmpty()) {
					server = null;
					world = null;
				}
				if (world == null || world.isEmpty()) {
					world = null;
				}
				group.removePermission(permission);
				Permission perm = new Permission(server, world, node, active);
				group.setPermission(perm);
				GroupManager.updatePermissions(group);
				fillPermissionList();
			}
		});
	}

	private void onPermissionRemove(Permission permission) {
		if (group == null) {
			return;
		}
		if (permission == null) {
			return;
		}
		FormBase form = new FormPermissionRemove(parent, group, permission);
		form.showForm(fr -> {
			if (fr.getType() == FormResultType.YES) {
				group.removePermission(permission);
				GroupManager.updatePermissions(group);
				fillPermissionList();
			}
		});

	}

	private void fillPermissionList() {
		permissionList.getItems().clear();

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
		} catch (Exception e) {
		}
		for (Permission permission : group.getPermissions()) {
			if (!searchNode.isEmpty() && (permission.getNode() == null || !permission.getNode().contains(searchNode)
					&& (nodePattern != null && !nodePattern.matcher(permission.getNode()).find()))) {
				continue;
			}
			if (!searchWorld.isEmpty() && (permission.getWorld() == null || !permission.getWorld().contains(searchWorld)
					&& (worldPattern != null && !worldPattern.matcher(permission.getWorld()).find()))) {
				continue;
			}
			if (!searchServer.isEmpty()
					&& (permission.getServer() == null || !permission.getServer().contains(searchServer)
							&& (serverPattern != null && !serverPattern.matcher(permission.getServer()).find()))) {
				continue;
			}
			permissionList.getItems().add(permission);
		}
	}

}
