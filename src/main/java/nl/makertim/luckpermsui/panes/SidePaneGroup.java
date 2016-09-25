package nl.makertim.luckpermsui.panes;

import java.util.function.Consumer;
import java.util.regex.Pattern;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import nl.makertim.luckpermsui.elements.LuckPermTextField;
import nl.makertim.luckpermsui.elements.PermissionStringConverter;
import nl.makertim.luckpermsui.elements.TexturedButton;
import nl.makertim.luckpermsui.form.FormBase;
import nl.makertim.luckpermsui.form.FormResultType;
import nl.makertim.luckpermsui.internal.Group;
import nl.makertim.luckpermsui.elements.LuckPermLabel;
import nl.makertim.luckpermsui.internal.GroupManager;
import nl.makertim.luckpermsui.internal.Permission;

public class SidePaneGroup extends VBox {

	private ViewManager parent;
	private Group group;

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
		TextField searchServer = new LuckPermTextField();
		searchServer.setPromptText("Search filter server.");
		TextField searchWorld = new LuckPermTextField();
		searchWorld.setPromptText("Search filter world.");
		TextField searchNode = new LuckPermTextField();
		searchNode.setPromptText("Search filter node.");

		ListView<Permission> permissionList = new ListView<>();
		permissionList.setCellFactory(lv -> {
			TextFieldListCell<Permission> cell = new TextFieldListCell<>();
			StringConverter<Permission> converter = new PermissionStringConverter(lv.getItems());
			cell.setConverter(converter);
			return cell;
		});
		fillPermissionList(permissionList, "", "", "");
		permissionList.setPrefWidth(250);
		permissionList.setPrefHeight(687);

		addButton.setOnMouseClicked((Consumer<MouseEvent>) click -> onPermissionAdd());

		searchServer.textProperty().addListener(onChange -> fillPermissionList(permissionList, searchServer.getText(),
			searchWorld.getText(), searchNode.getText()));
		searchWorld.textProperty().addListener(onChange -> fillPermissionList(permissionList, searchServer.getText(),
			searchWorld.getText(), searchNode.getText()));
		searchNode.textProperty().addListener(onChange -> fillPermissionList(permissionList, searchServer.getText(),
			searchWorld.getText(), searchNode.getText()));

		topRightCorner.getChildren().addAll(addButton, changeButton, removeButton);
		topLine.setLeft(nameLabel);
		topLine.setRight(topRightCorner);
		searchers.getChildren().addAll(searchServer, searchWorld, searchNode);
		getChildren().addAll(topLine, searchers, permissionList);

		Platform.runLater(() -> {
			searchServer.setPrefWidth(Short.MAX_VALUE);
			searchWorld.setPrefWidth(Short.MAX_VALUE);
			searchNode.setPrefWidth(Short.MAX_VALUE);
		});
	}

	private void onPermissionAdd() {
		if (group == null) {
			return;
		}
		FormBase form = new FormPermissionAdd(parent, group);
		form.showForm(fr -> {
			if (fr.getType() == FormResultType.OK) {
				Object[] vaules = fr.getResult();
				String node = (String) vaules[0];
				String server = (String) vaules[1];
				String world = (String) vaules[2];
				boolean active = (boolean) vaules[3];
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
			}
		});
	}

	private void fillPermissionList(ListView<Permission> permissionList, String serverFilter, String worldFilter, String nodeFilter) {
		permissionList.getItems().clear();
		Pattern serverPattern = null;
		Pattern worldPattern = null;
		Pattern nodePattern = null;
		try {
			serverPattern = Pattern.compile(serverFilter);
			worldPattern = Pattern.compile(worldFilter);
			nodePattern = Pattern.compile(nodeFilter);
		} catch (Exception e) {
		}
		for (Permission permission : group.getPermissions()) {
			if (!nodeFilter.isEmpty() && (permission.getNode() == null || !permission.getNode().contains(nodeFilter)
					&& (nodePattern != null && !nodePattern.matcher(permission.getNode()).find()))) {
				continue;
			}
			if (!worldFilter.isEmpty() && (permission.getWorld() == null || !permission.getWorld().contains(worldFilter)
					&& (worldPattern != null && !worldPattern.matcher(permission.getWorld()).find()))) {
				continue;
			}
			if (!serverFilter.isEmpty()
					&& (permission.getServer() == null || !permission.getServer().contains(serverFilter)
							&& (serverPattern != null && !serverPattern.matcher(permission.getServer()).find()))) {
				continue;
			}
			permissionList.getItems().add(permission);
		}
	}

}
