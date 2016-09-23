package nl.makertim.luckpermsui.popup;

import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import nl.makertim.luckpermsui.util.StringUtil;

public class FormBase extends Stage {

	private BorderPane pane;
	protected GridPane content;
	protected Control[] controls;
	protected FormResultType result = FormResultType.CLOSE;

	public FormBase(Window parent, String name, List<FormItem> items, List<FormResultType> buttons) {
		super(StageStyle.UTILITY);
		pane = new BorderPane();
		pane.setPadding(new Insets(10, 10, 10, 10));
		content = new GridPane();
		Scene scene = new Scene(pane);
		setTitle(name);
		setScene(scene);

		content.setVgap(8);
		content.setHgap(8);
		content.setAlignment(Pos.CENTER_LEFT);

		controls = new Control[items.size()];
		for (int i = 0; i < items.size(); i++) {
			FormItem item = items.get(i);
			Label controlName = new Label(item.getName());
			controls[i] = item.getItem();
			GridPane.setConstraints(controlName, 0, i);
			GridPane.setConstraints(controls[i], 1, i);
			content.getChildren().addAll(controlName, controls[i]);
		}

		HBox buttonRow = new HBox(8);
		for (FormResultType type : buttons) {
			Button button = new Button(StringUtil.readableEnum(type));
			button.setOnAction(click -> {
				result = type;
				close();
			});
			buttonRow.getChildren().add(button);
		}

		pane.setCenter(content);
		pane.setBottom(buttonRow);
		centerOnParent(parent);
	}

	private void centerOnParent(Window parent) {
		if (parent == null) {
			centerOnScreen();
			return;
		}
		setX(parent.getX() + (parent.getWidth() / 2) /*- (getWidth() / 2)*/);
		setY(parent.getY() + (parent.getHeight() / 2) /*- (getHeight() / 2)*/);
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
			} else {
				ret[i] = control;
			}
		}
		return ret;
	}

	public FormResult showForm() {
		showAndWait();
		return new FormResult(result, getValues());
	}
}
