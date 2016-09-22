package nl.makertim.luckpermsui.panes;

import com.google.gson.JsonElement;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import nl.makertim.luckpermsui.internal.Group;

import java.util.Map;
import java.util.regex.Pattern;

public class GroupSideView extends VBox {

	private Group group;

	public GroupSideView(Group group) {
		super();
		this.group = group;
		setup();
	}

	private void setup() {
		Label nameLabel = new Label(String.format("Permissions of %s.", group.getName()));
		TextField search = new TextField();
		search.setPromptText("Search for permission.");
		search.setPrefWidth(200);

		ListView<String> permissionList = new ListView<>();
		fillPermissionList(permissionList, "");
		permissionList.setPrefWidth(250);
		permissionList.setPrefHeight(727);

		search.textProperty().addListener(onChange -> fillPermissionList(permissionList, search.getText()));

		getChildren().addAll(nameLabel, search, permissionList);
	}

	private void fillPermissionList(ListView<String> permissionList, String filter) {
		permissionList.getItems().clear();
		Pattern pattern = null;
		try {
			pattern = Pattern.compile(filter);
		} catch (Exception e) {
		}
		for (String permission : group.getPermissions()) {
			if (filter.isEmpty() || permission.contains(filter)
					|| (pattern != null && pattern.matcher(permission).find())) {
				permissionList.getItems().add(permission);
			}
		}
	}

}
