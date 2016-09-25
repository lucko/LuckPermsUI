package nl.makertim.luckpermsui.panes;

import java.util.regex.Pattern;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import nl.makertim.luckpermsui.elements.LuckPermTextField;
import nl.makertim.luckpermsui.elements.PermissionStringConverter;
import nl.makertim.luckpermsui.internal.Group;
import nl.makertim.luckpermsui.elements.LuckPermLabel;
import nl.makertim.luckpermsui.internal.Permission;

public class SidePaneGroup extends VBox {

	private Group group;

	public SidePaneGroup(Group group) {
		super();
		this.group = group;
		setup();
	}

	private void setup() {
		Label nameLabel = new LuckPermLabel(String.format("Permissions of %s.", group.getName()));
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

		searchServer.textProperty().addListener(onChange -> fillPermissionList(permissionList, searchServer.getText(),
			searchWorld.getText(), searchNode.getText()));
		searchWorld.textProperty().addListener(onChange -> fillPermissionList(permissionList, searchServer.getText(),
			searchWorld.getText(), searchNode.getText()));
		searchNode.textProperty().addListener(onChange -> fillPermissionList(permissionList, searchServer.getText(),
			searchWorld.getText(), searchNode.getText()));

		searchers.getChildren().addAll(searchServer, searchWorld, searchNode);
		getChildren().addAll(nameLabel, searchers, permissionList);

		Platform.runLater(() -> {
			searchServer.setPrefWidth(Short.MAX_VALUE);
			searchWorld.setPrefWidth(Short.MAX_VALUE);
			searchNode.setPrefWidth(Short.MAX_VALUE);
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
