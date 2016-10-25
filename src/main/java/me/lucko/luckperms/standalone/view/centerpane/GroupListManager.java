package me.lucko.luckperms.standalone.view.centerpane;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;

import java.util.Map;
import java.util.regex.Pattern;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import me.lucko.luckperms.common.groups.Group;
import me.lucko.luckperms.standalone.controller.GroupController;
import me.lucko.luckperms.standalone.factory.SimpleViewFactory;
import me.lucko.luckperms.standalone.util.form.Updatable;
import me.lucko.luckperms.standalone.view.elements.GroupTreeObject;
import me.lucko.luckperms.standalone.view.elements.TexturedButton;
import me.lucko.luckperms.standalone.view.scene.Manager;

public class GroupListManager extends VBox implements Updatable {

    private Manager parent;
    private TextField search;
    private TreeTableView<GroupTreeObject> groupList;

    private GroupController controller;

    public GroupListManager(Manager manager) {
        parent = manager;
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
        fillGroupView();

        search.textProperty().addListener(change -> fillGroupView());
        addButton.setOnMouseClicked(click -> onAddGroup());
        refreshButton.setOnMouseClicked(click -> onRefresh());
        removeButton.setOnMouseClicked(click -> onRemoveGroup());
        groupList.getSelectionModel().selectedIndexProperty().addListener(change -> updateSelected());

        topLine.getChildren().addAll(search, addButton, refreshButton, removeButton);
        getChildren().addAll(topLine, groupList);
    }

    public void registerController(GroupController controller) {
        this.controller = controller;
    }

    private void onAddGroup() {
        controller.addGroup(parent);
    }

    private void onRemoveGroup() {
        Group group = fromItem(groupList.getSelectionModel().getSelectedItem());
        if (group == null) {
            return;
        }
        controller.removeGroup(parent, group);
    }

    private void onRefresh() {
        controller.getBase().reloadGroups();
        update();
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

        TreeItem<GroupTreeObject> treeItem = new TreeItem<>();
        treeItem.setExpanded(true);
        groupList.setRoot(treeItem);
        groupList.setShowRoot(false);
        groupList.setPrefHeight(Short.MAX_VALUE);
    }

    private void fillGroupView() {
        Map<String, Group> groups = parent.getController().getBase().getGroupManager().getAll();
        groupList.getRoot().getChildren().clear();

        groups.values().stream()
                .filter(group -> {
                    String filterLower = search.getText().toLowerCase();
                    Pattern pattern = Pattern.compile(".+");
                    try {
                        pattern = Pattern.compile(filterLower);
                    } catch (Exception ignored) {
                    }
                    String groupName = group.getDisplayName().toLowerCase();
                    return filterLower.isEmpty() || pattern.matcher(groupName).find() || groupName.contains(filterLower);
                })
                .forEach(group -> groupList.getRoot().getChildren().add(new TreeItem<>(new GroupTreeObject(group))));
    }

    @Override
    public void update() {
        fillGroupView();
    }

    private Group fromItem(TreeItem<GroupTreeObject> item) {
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
