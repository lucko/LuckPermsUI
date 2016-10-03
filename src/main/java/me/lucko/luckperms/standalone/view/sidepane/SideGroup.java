package me.lucko.luckperms.standalone.view.sidepane;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import me.lucko.luckperms.LPStandaloneApp;
import me.lucko.luckperms.api.MetaUtils;
import me.lucko.luckperms.api.Node;
import me.lucko.luckperms.api.implementation.internal.GroupLink;
import me.lucko.luckperms.exceptions.ObjectAlreadyHasException;
import me.lucko.luckperms.exceptions.ObjectLacksException;
import me.lucko.luckperms.groups.Group;
import me.lucko.luckperms.standalone.util.ColoredLine;
import me.lucko.luckperms.standalone.util.elements.LuckPermLabel;
import me.lucko.luckperms.standalone.util.elements.LuckPermTextField;
import me.lucko.luckperms.standalone.util.elements.TexturedButton;
import me.lucko.luckperms.standalone.util.form.FormBase;
import me.lucko.luckperms.standalone.util.form.FormResultType;
import me.lucko.luckperms.standalone.view.popup.PermissionAdd;
import me.lucko.luckperms.standalone.view.popup.PermissionChange;
import me.lucko.luckperms.standalone.view.popup.PermissionRemove;
import me.lucko.luckperms.standalone.view.scene.Manager;

public class SideGroup extends VBox {

	private Manager parent;
	private Group group;
	private TableView<Node> permissionList;
	TextField searchServer;
	TextField searchWorld;
	TextField searchNode;

	public SideGroup(Manager parent, Group group) {
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

		String prefix = MetaUtils.getPrefix(new GroupLink(group), null, null, true);
		if (prefix != null) {
			ColoredLine prefixLine = new ColoredLine("Prefix = " + prefix);
			prefixLine.setFont(LPStandaloneApp.FONT);
			groupInfo.getChildren().add(prefixLine);
		}

		if (group.getGroupNames().size() > 0) {
			groupInfo.getChildren().add(new LuckPermLabel("Inherits from:"));
			for (String parentGroup : group.getGroupNames()) {
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
		getChildren().addAll(topLine, groupInfo, searchers, permissionList);

		Platform.runLater(() -> {
			searchServer.setPrefWidth(Short.MAX_VALUE);
			searchWorld.setPrefWidth(Short.MAX_VALUE);
			searchNode.setPrefWidth(Short.MAX_VALUE);
		});
	}

	private void setupTable() {
		permissionList = new TableView<>();
		permissionList.setRowFactory(rf -> new TableRow<Node>() {
			@Override
			protected void updateItem(Node permission, boolean empty) {
				if (permission == null) {
					setTextFill(Color.BLACK);
				} else if (permission.getValue()) {
					setTextFill(Color.GREEN);
				} else {
					setTextFill(Color.RED);
				}
			}
		});

		TableColumn node = new TableColumn("Node");
		node.setCellValueFactory(new PropertyValueFactory<Node, UUID>("node"));
		TableColumn server = new TableColumn("Server");
		server.setCellValueFactory(new PropertyValueFactory<Node, UUID>("server"));
		TableColumn world = new TableColumn("World");
		world.setCellValueFactory(new PropertyValueFactory<Node, UUID>("world"));
		TableColumn active = new TableColumn("Allowed");
		active.setCellValueFactory(new PropertyValueFactory<Node, UUID>("active"));

		permissionList.getColumns().addAll(node, server, world, active);

		fillPermissionList();
		permissionList.setPrefWidth(250);
		permissionList.setPrefHeight(687);
	}

	private void onPermissionAdd() {
		if (group == null) {
			return;
		}
		FormBase form = new PermissionAdd(parent, group);
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
				/*
				 * Permission perm = new Permission(server, world, node,
				 * active); group.setPermission(perm);
				 * GroupManager.updatePermissions(group);
				 */
				// TODO
				fillPermissionList();
			}
		});
	}

	private void onPermissionChange(Node permission) {
		if (group == null || permission == null) {
			return;
		}
		FormBase form = new PermissionChange(parent, group, permission);
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
				try {
					group.unsetPermission(permission);
				} catch (ObjectLacksException ignored) {

				}

				Node perm = new me.lucko.luckperms.core.Node.Builder(node).setServer(server).setWorld(world)
						.setValue(active).build();
				try {
					group.setPermission(perm);
				} catch (ObjectAlreadyHasException e) {
					e.printStackTrace();
				}
				// TODO save
				fillPermissionList();
			}
		});
	}

	private void onPermissionRemove(Node permission) {
		if (group == null) {
			return;
		}
		if (permission == null) {
			return;
		}
		FormBase form = new PermissionRemove(parent, group, permission);
		form.showForm(fr -> {
			if (fr.getType() == FormResultType.YES) {
				// TODO remove & update permission
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
			permissionList.getItems().add(permission);
		}
	}

}
