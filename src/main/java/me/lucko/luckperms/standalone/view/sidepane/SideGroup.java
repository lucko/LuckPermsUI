package me.lucko.luckperms.standalone.view.sidepane;

import java.util.function.Consumer;
import java.util.regex.Pattern;

import com.jfoenix.controls.JFXTextField;

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
import me.lucko.luckperms.groups.Group;
import me.lucko.luckperms.standalone.controller.GroupController;
import me.lucko.luckperms.standalone.util.ColoredLine;
import me.lucko.luckperms.standalone.util.OptionalPropertyValueFactory;
import me.lucko.luckperms.standalone.view.elements.TexturedButton;
import me.lucko.luckperms.standalone.view.scene.Manager;

public class SideGroup extends VBox {

    private TableView<Node> permissionList;
    private TextField searchServer;
    private TextField searchWorld;
    private TextField searchNode;

    private Manager parent;
    private Group group;
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
        TexturedButton addButton = new TexturedButton("assets/images/add.png", 24,
                String.format("Add a permission to the group %s.", group.getName()));
        TexturedButton changeButton = new TexturedButton("assets/images/change.png", 24, "Change selected permission.");
        TexturedButton removeButton = new TexturedButton("assets/images/remove.png", 24,
                String.format("Remove selected permission.", group.getName()));

        HBox searchers = new HBox();
        searchServer = new JFXTextField();
        searchServer.setPromptText("Search filter server.");
        searchWorld = new JFXTextField();
        searchWorld.setPromptText("Search filter world.");
        searchNode = new JFXTextField();
        searchNode.setPromptText("Search filter node.");

        VBox groupInfo = new VBox(1);
        groupInfo.setPadding(new Insets(3, 0, 3, 3));

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
        node.setCellValueFactory(new PropertyValueFactory<>("permission"));
        TableColumn server = new TableColumn("Server");
        server.setCellValueFactory(new OptionalPropertyValueFactory<>("server"));
        TableColumn world = new TableColumn("World");
        world.setCellValueFactory(new OptionalPropertyValueFactory<>("world"));
        TableColumn active = new TableColumn("Allowed");
        active.setCellValueFactory(new PropertyValueFactory<>("active"));

        permissionList.getColumns().addAll(node, server, world, active);

        fillPermissionList();
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

    public void fillPermissionList() {
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

    public void registerController(GroupController controller) {
        this.controller = controller;
    }
}
