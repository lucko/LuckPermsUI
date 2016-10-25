package me.lucko.luckperms.standalone.view.centerpane;

import com.jfoenix.controls.JFXTextField;

import java.util.UUID;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import me.lucko.luckperms.common.users.User;
import me.lucko.luckperms.standalone.util.UsernameGetter;
import me.lucko.luckperms.standalone.util.form.FormBase;
import me.lucko.luckperms.standalone.util.form.FormResultType;
import me.lucko.luckperms.standalone.view.elements.TexturedButton;
import me.lucko.luckperms.standalone.view.popup.UserNew;
import me.lucko.luckperms.standalone.view.scene.Manager;

public class UserManager extends BorderPane {

    private Manager parent;
    private TableView<User> userList;
    private TextField nameSearcher;

    public UserManager(Manager manager) {
        parent = manager;
        setup();
    }

    private void setup() {
        HBox topLane = new HBox();
        nameSearcher = new JFXTextField();
        nameSearcher.setPrefWidth(Short.MAX_VALUE);
        nameSearcher.setPromptText("Name filter.");
        TexturedButton addButton = new TexturedButton("assets/images/add.png", 24, "Force add a user.");
        TexturedButton updateButton = new TexturedButton("assets/images/update.png", 24, "Refresh username.");
        TexturedButton refreshButton = new TexturedButton("assets/images/refresh.png", 24, "Refresh list.");
        TexturedButton removeButton = new TexturedButton("assets/images/remove.png", 24, "Remove user.");

        setupTableView();
        filterList();

        nameSearcher.textProperty().addListener(change -> filterList());
        addButton.setOnMouseClicked(click -> onUserAdd());
        updateButton.setOnMouseClicked(click -> refreshUsername(userList.getSelectionModel().getSelectedItem()));
        refreshButton.setOnMouseClicked(click -> filterList());
        removeButton.setOnMouseClicked(click -> {
        });

        topLane.getChildren().addAll(nameSearcher, addButton, updateButton, refreshButton, removeButton);
        setTop(topLane);
        setCenter(userList);
    }

    private void onUserAdd() {
        FormBase newUserForm = new UserNew(parent, nameSearcher.getText());
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
        // TODO: UserManager.updateUser(user);
        filterList();
    }

    private void setupTableView() {
        userList = new TableView<>();
        userList.setPrefHeight(704);

        TableColumn<User, UUID> uuid = new TableColumn<>("UUID");
        uuid.setCellValueFactory(new PropertyValueFactory<>("uuid"));
        TableColumn<User, String> name = new TableColumn<>("Username");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<User, String> group = new TableColumn<>("Group");
        group.setCellValueFactory(new PropertyValueFactory<>("primaryGroup"));

        userList.getColumns().addAll(uuid, name, group);
    }

    private void filterList() {
        userList.getItems().clear();
        // TODO:
        // userList.getItems().addAll(UserManager.getUsers(nameSearcher.getText()));
    }

}
