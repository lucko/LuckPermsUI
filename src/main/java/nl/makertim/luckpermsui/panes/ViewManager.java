package nl.makertim.luckpermsui.panes;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.Rectangle;

public class ViewManager extends StackPane {

	BorderPane mainLayer;
	VBox menu;
	Pane mainView;
	Pane sideView;
	StackPane overlay;

	public ViewManager() {
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

	private void setup() {
		menu = new VBox(10);
		menu.setPadding(new Insets(0, 4, 0, 0));
		menu.setMinWidth(90);
		mainView = new StackPane();
		sideView = new StackPane();
		sideView.setMinWidth(100);

		Button groupButton = new Button("Groups");
		Button userButton = new Button("User manager");

		groupButton.setOnAction(action -> setMainView(new PaneGroup(this)));
		// userButton.setOnAction(action -> setMainView(new UserView(this)));

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
			System.out.println(hCenter);
			overlay.getChildren().addAll(fade, hCenter);
		}
	}
}
