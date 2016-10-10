package me.lucko.luckperms.standalone.view.scene;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import lombok.Getter;
import me.lucko.luckperms.standalone.controller.ManagerController;
import me.lucko.luckperms.standalone.view.centerpane.GroupListManager;
import me.lucko.luckperms.standalone.view.centerpane.UserManager;
import me.lucko.luckperms.standalone.view.elements.RaisedButton;

public class Manager extends StackPane {

	private BorderPane mainLayer;
	private VBox menu;
	private Pane mainView;
	private Pane sideView;
	private StackPane overlay;

	@Getter
	private ManagerController controller;

	public Manager() {
		mainLayer = new BorderPane();
		overlay = new StackPane();
		overlay.setPickOnBounds(false);
		overlay.setAlignment(Pos.CENTER);
		setup();
		setWidth(600);
		getChildren().addAll(mainLayer, overlay);
		widthProperty().addListener(resize -> onResize());
		heightProperty().addListener(resize -> onResize());
		Platform.runLater(() -> onResize());
	}

	public void registerController(ManagerController controller) {
		this.controller = controller;
	}

	private void setup() {
		menu = new VBox(10);
		menu.setPadding(new Insets(0, 4, 0, 4));
		menu.setMinWidth(90);
		mainView = new StackPane();
		sideView = new StackPane();
		sideView.setMinWidth(100);

		Button groupButton = new RaisedButton("Groups");
		Button userButton = new RaisedButton("User manager");

		groupButton.setOnAction(action -> setMainView(new GroupListManager(this)));
		userButton.setOnAction(action -> setMainView(new UserManager(this)));

		menu.getChildren().addAll(groupButton, userButton);
		mainLayer.setLeft(menu);
		mainLayer.setCenter(mainView);
		mainLayer.setRight(sideView);
	}

	private void onResize() {
		mainView.setMinWidth(((getWidth() / 5) * 3) - menu.getWidth() / 2);
		mainView.setMaxWidth(((getWidth() / 5) * 3) - menu.getWidth() / 2);
		sideView.setMinWidth(((getWidth() / 5) * 2) - menu.getWidth() / 2);
		sideView.setMaxWidth(((getWidth() / 5) * 2) - menu.getWidth() / 2);
	}

	public void setMainView(Pane pane) {
		mainView.getChildren().clear();
		setSideView(null);
		if (pane != null) {
			mainView.getChildren().add(pane);
		}
	}

	public void setSideView(Pane pane) {
		sideView.getChildren().clear();
		if (pane != null) {
			sideView.getChildren().add(pane);
		}
	}

	public void setOverlay(Pane pane) {
		overlay.getChildren().clear();
		if (pane != null) {
			Rectangle fade = new Rectangle(mainView.getScene().getWidth(), mainView.getScene().getHeight());
			Stop[] stops = {new Stop(0, new Color(0.9, 0.9, 0.9, 0.9)), new Stop(1, new Color(0.1, 0.1, 0.1, 0.1))};
			fade.setFill(new RadialGradient(0, 0, 0.5, 0.5, 1.5, true, CycleMethod.NO_CYCLE, stops));
			VBox vCenter = new VBox(pane);
			HBox hCenter = new HBox(vCenter);
			vCenter.setAlignment(Pos.CENTER);
			hCenter.setAlignment(Pos.CENTER);
			overlay.getChildren().addAll(fade, hCenter);
		}
	}
}
