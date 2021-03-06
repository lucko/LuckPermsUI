package me.lucko.luckperms.standalone.util.form;

import java.util.List;
import java.util.function.Consumer;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import me.lucko.luckperms.standalone.util.StringUtil;
import me.lucko.luckperms.standalone.view.elements.RaisedButton;
import me.lucko.luckperms.standalone.view.scene.Manager;

public class FormBase extends BorderPane {

    private Manager view;
    protected GridPane content;
    protected Control[] controls;
    protected FormResultType result = FormResultType.CLOSE;
    private Consumer<FormResult> onFormComplete;

    public FormBase(Manager view, String name, List<FormItem> items, List<FormResultType> buttons) {
        this.view = view;
        setPadding(new Insets(10, 10, 10, 10));

        content = new GridPane();
        content.setVgap(8);
        content.setHgap(8);
        content.setAlignment(Pos.CENTER_LEFT);

        Label label = new Label(name);
        GridPane.setConstraints(label, 0, 0);
        content.getChildren().add(label);

        controls = new Control[items.size()];
        for (int i = 0; i < items.size(); i++) {
            FormItem item = items.get(i);
            Label controlName = new Label(item.getName());
            controls[i] = item.getItem();
            GridPane.setConstraints(controlName, 0, i + 1);
            GridPane.setConstraints(controls[i], 1, i + 1);
            content.getChildren().addAll(controlName, controls[i]);
            Platform.runLater(() -> controls[0].requestFocus());
        }

        HBox buttonRow = new HBox(8);
        for (FormResultType type : buttons) {
            Button button = new RaisedButton(StringUtil.readableEnum(type));
            button.setOnAction(click -> {
                result = type;
                view.setOverlay(null);
                if (onFormComplete != null) {
                    onFormComplete.accept(new FormResult(result, getValues()));
                }
            });
            buttonRow.getChildren().add(button);
        }

        setCenter(content);
        setBottom(buttonRow);
    }

    protected Object[] getValues() {
        Object[] ret = new Object[controls.length];
        for (int i = 0; i < controls.length; i++) {
            Control control = controls[i];
            if (control instanceof Pagination) {
                ret[i] = ((Pagination) control).getCurrentPageIndex();
            } else if (control instanceof ProgressIndicator) {
                ret[i] = ((ProgressIndicator) control).getProgress();
            } else if (control instanceof ComboBoxBase) {
                ret[i] = ((ComboBoxBase) control).getValue();
            } else if (control instanceof TreeView) {
                ret[i] = ((TreeView) control).getSelectionModel().getSelectedItem();
            } else if (control instanceof TreeTableView) {
                ret[i] = ((TreeTableView) control).getSelectionModel().getSelectedItem();
            } else if (control instanceof ChoiceBox) {
                ret[i] = ((ChoiceBox) control).getSelectionModel().getSelectedItem();
            } else if (control instanceof ListView) {
                ret[i] = ((ListView) control).getSelectionModel().getSelectedItem();
            } else if (control instanceof Spinner) {
                ret[i] = ((Spinner) control).getValue();
            } else if (control instanceof TextInputControl) {
                ret[i] = ((TextInputControl) control).getText();
            } else if (control instanceof CheckBox) {
                ret[i] = ((CheckBox) control).isSelected();
            } else {
                ret[i] = control;
            }
        }
        return ret;
    }

    public void showForm(Consumer<FormResult> onFormComplete) {
        this.onFormComplete = onFormComplete;
        view.setOverlay(this);
    }
}
