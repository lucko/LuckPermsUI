package nl.makertim.luckpermsui.panes;

import java.util.UUID;
import java.util.function.Consumer;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import nl.makertim.luckpermsui.elements.LuckPermTextField;
import nl.makertim.luckpermsui.elements.ObjectStringConverter;
import nl.makertim.luckpermsui.elements.TexturedButton;
import nl.makertim.luckpermsui.form.FormBase;
import nl.makertim.luckpermsui.form.FormResultType;
import nl.makertim.luckpermsui.internal.User;
import nl.makertim.luckpermsui.internal.UserManager;
import nl.makertim.luckpermsui.util.UsernameGetter;

public class PaneUser extends BorderPane {

	private ViewManager parent;
	private TableView<User> userList;
	private TextField nameSearcher;

	public PaneUser(ViewManager viewManager) {
		parent = viewManager;
		setup();
	}

	private void setup() {
		HBox topLane = new HBox();
		nameSearcher = new LuckPermTextField();
		nameSearcher.setPrefWidth(Short.MAX_VALUE);
		nameSearcher.setPromptText("Name filter.");
		TexturedButton addButton = new TexturedButton("assets/images/add.png", 24, "Force add a user.");
		TexturedButton updateButton = new TexturedButton("assets/images/update.png", 24, "Refresh username.");
		TexturedButton refreshButton = new TexturedButton("assets/images/refresh.png", 24, "Refresh list.");
		TexturedButton removeButton = new TexturedButton("assets/images/remove.png", 24, "Remove user.");

		setupTableView();
		filterList();

		nameSearcher.textProperty().addListener(change -> filterList());
		addButton.setOnMouseClicked((Consumer<MouseEvent>) click -> onUserAdd());
		updateButton.setOnMouseClicked(
			(Consumer<MouseEvent>) click -> refreshUsername(userList.getSelectionModel().getSelectedItem()));
		refreshButton.setOnMouseClicked((Consumer<MouseEvent>) click -> filterList());
		removeButton.setOnMouseClicked((Consumer<MouseEvent>) click -> {
		});

		topLane.getChildren().addAll(nameSearcher, addButton, updateButton, refreshButton, removeButton);
		setTop(topLane);
		setCenter(userList);
	}

	private void onUserAdd() {
		FormBase newUserForm = new FormUserNew(parent, nameSearcher.getText());
		newUserForm.showForm(fr -> {
			if (fr.getType() == FormResultType.OK) {
				Object[] result = fr.getResult();
				// TODO
			}
		});
	}

	private void refreshUsername(User user) {
		if (user == null) {
			return;
		}
		String newName = UsernameGetter.currentName(user.getUuid());
		user.setName(newName);
		UserManager.updateUser(user);
		filterList();
	}

	private void setupTableView() {
		userList = new TableView<>();
		userList.setPrefHeight(704);

		TableColumn uuid = new TableColumn("UUID");
		uuid.setCellValueFactory(new PropertyValueFactory<User, UUID>("uuid"));
		TableColumn name = new TableColumn("Username");
		name.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
		TableColumn group = new TableColumn("Group");
		group.setCellValueFactory(new PropertyValueFactory<User, String>("defaultGroup"));

		userList.getColumns().addAll(uuid, name, group);
	}

	private void filterList() {
		userList.getItems().clear();
		userList.getItems().addAll(UserManager.getUsers(nameSearcher.getText()));
	}

}
